package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.show.action

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.active.LastActive
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.DateFeedbackStatService
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.EditStatisticService
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.ProjectsStatisticService
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.TasksStatisticService
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task.converter.TasksStatisticConverter
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification.KotlinFeedbackNotificationGroup
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware

class StatisticPluginShow : AnAction(), DumbAware {

    private val tasksStatisticService: TasksStatisticService = service()
    private val editRelevantStatisticService: EditStatisticService = service()
    private val dateFeedbackStatService: DateFeedbackStatService = service()
    private val projectsStatisticService: ProjectsStatisticService = service()
    private val tasksStatisticConverter: TasksStatisticConverter = TasksStatisticConverter()
    private val gsonPrettyPrinter: Gson = GsonBuilder().disableHtmlEscaping()
            .enableComplexMapKeySerialization().setPrettyPrinting().create()


    override fun actionPerformed(e: AnActionEvent) {

        val notification = Notification(
                KotlinFeedbackNotificationGroup.group.displayId,
                "Collected statistic",
                "<html>" +
                        "Is satisfies BeginnerUserType: ${BeginnerUserType.isUserSatisfiesUserType()}<br>" +
                        "Is satisfies SimpleUserType: ${SimpleUserType.isUserSatisfiesUserType()}<br>" +
                        "Is satisfies ActiveUserType: ${ActiveUserType.isUserSatisfiesUserType()}<br><br>" +
                        "Data sharing consent: ${isSendFusEnabled()}<br>" +
                        "Projects info: ${gsonPrettyPrinter.toJson(
                                projectsStatisticService.state!!.projectsStatisticState)}<br>" +
                        "Last active time: ${LastActive.lastActive}<br>" +
                        "Is Kotlin project: ${isKotlinProject(e.project!!)}<br>" +
                        "Is EAP Intellij IDEA: ${isIntellijIdeaEAP()}<br>" +
                        "Is EAP Kotlin plugin: ${isKotlinPluginEAP()}<br>" +
                        " " +
                        "Tasks info - " +
                        gsonPrettyPrinter.toJson(
                                tasksStatisticService.state!!.projectsTasksInfo
                        ) + "<br>" +
                        "Count of Kotlin file editing - " +
                        "${gsonPrettyPrinter.toJson(
                                editRelevantStatisticService.state!!.countEditKotlinFile)}<br>" +
                        "Last date for show feedback notification " +
                        "${gsonPrettyPrinter.toJson(
                                dateFeedbackStatService.state!!.dateShowFeedbackNotification)}<br>" +
                        "Last date for send feedback ${gsonPrettyPrinter.toJson(
                                dateFeedbackStatService.state!!.dateSendFeedback)}<br>" +
                        "Last date for close or cancel feedback dialog ${gsonPrettyPrinter.toJson(
                                dateFeedbackStatService.state!!.dateCloseFeedbackDialog)}<html>",
                NotificationType.INFORMATION
        )

        notification.notify(e.project)
    }
}