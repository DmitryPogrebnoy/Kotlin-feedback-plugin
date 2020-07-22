package com.github.dmitrypogrebnoy.feedbacktest.action

import com.github.dmitrypogrebnoy.feedbacktest.services.DateFeedbackStatService
import com.github.dmitrypogrebnoy.feedbacktest.services.EditRelevantStatisticService
import com.github.dmitrypogrebnoy.feedbacktest.services.TasksStatisticService
import com.github.dmitrypogrebnoy.feedbacktest.state.editor.converter.EditorStatisticConverter
import com.github.dmitrypogrebnoy.feedbacktest.state.task.converter.TasksStatisticConverter
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware

class StatisticPluginShow : AnAction(), DumbAware {

    private val tasksStatisticService: TasksStatisticService = service()
    private val editRelevantStatisticService: EditRelevantStatisticService = service()
    private val dateFeedbackStatService: DateFeedbackStatService = service()
    private val tasksStatisticConverter: TasksStatisticConverter = TasksStatisticConverter()

    override fun actionPerformed(e: AnActionEvent) {
        val notification = Notification(
                "Show Statistic",
                "Collected statistic",
                "<html>Tasks info - " +
                        tasksStatisticConverter.toString(
                                tasksStatisticService.state!!.projectsTasksInfo
                        ) +
                        " <br> Count of Kotlin file editing - " +
                        "${EditorStatisticConverter().toString(editRelevantStatisticService.state!!.countEditKotlinFile)}" +
                        "<br> ${dateFeedbackStatService.state!!.showFeedbackNotificationDate}" +
                        "<br> ${dateFeedbackStatService.state!!.sendFeedbackDate}<html>",
                NotificationType.INFORMATION
        )

        notification.notify(e.project)
    }
}