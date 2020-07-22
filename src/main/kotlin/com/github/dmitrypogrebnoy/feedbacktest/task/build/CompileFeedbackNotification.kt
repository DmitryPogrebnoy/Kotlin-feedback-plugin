package com.github.dmitrypogrebnoy.feedbacktest.task.build

import com.github.dmitrypogrebnoy.feedbacktest.notification.FeedbackNotification
import com.github.dmitrypogrebnoy.feedbacktest.services.DateFeedbackStatService
import com.github.dmitrypogrebnoy.feedbacktest.services.EditRelevantStatisticService
import com.github.dmitrypogrebnoy.feedbacktest.services.TasksStatisticService
import com.github.dmitrypogrebnoy.feedbacktest.state.editor.EditInfo
import com.github.dmitrypogrebnoy.feedbacktest.state.show.notificationFeedback.DateFeedbackState
import com.github.dmitrypogrebnoy.feedbacktest.state.task.MAKE_TASK_NAME
import com.github.dmitrypogrebnoy.feedbacktest.state.task.ProjectTask
import com.github.dmitrypogrebnoy.feedbacktest.state.task.TasksStatisticState
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.application.ex.ApplicationInfoEx
import com.intellij.openapi.compiler.CompileContext
import com.intellij.openapi.compiler.CompileTask
import com.intellij.openapi.components.service
import com.intellij.openapi.extensions.PluginId
import java.time.LocalDate

class CompileFeedbackNotification : CompileTask {

    companion object {
        const val KOTLIN_PLUGIN_ID = "org.jetbrains.kotlin"

        // 30 days
        const val MIN_DAYS_SINCE_SEND_FEEDBACK: Long = 30

        // 5 days
        const val MIN_DAYS_SINCE_SHOW_FEEDBACK_NOTIFICATION: Long = 5

        // 5 times
        const val MIN_NUMBER_RELEVANT_EDITING_KOTLIN_FILE = 5

        // 2 minutes
        const val MIN_DURATION_COMPILE_TASK = 2
    }

    override fun execute(context: CompileContext?): Boolean {
        if (context != null) {
            val applicationInfoEx: ApplicationInfoEx = ApplicationInfoEx.getInstanceEx()

            val taskStatisticState: TasksStatisticState = service<TasksStatisticService>().state
                    ?: return true
            val dateFeedbackState: DateFeedbackState = service<DateFeedbackStatService>().state ?: return true
            val curProjectTask = ProjectTask(context.project.name, MAKE_TASK_NAME)
            if (taskStatisticState.projectsTasksInfo.containsKey(curProjectTask)) {
                val statisticInfo = taskStatisticState.projectsTasksInfo[curProjectTask]!!
                if (statisticInfo.lastTaskDurationTime > MIN_DURATION_COMPILE_TASK && checkRelevantNumberEditing() &&
                        //Set '!' for test          //Set '!' for test
                        (!applicationInfoEx.isEAP || !isKotlinPluginEAP()) && checkFeedbackDate()
                ) {
                    dateFeedbackState.showFeedbackNotificationDate = LocalDate.now()
                    FeedbackNotification(context.project).notificationNotify()
                }
            }
        }
        return true
    }

    private fun checkRelevantNumberEditing(): Boolean {
        val editorState: Map<LocalDate, EditInfo> = service<EditRelevantStatisticService>().state?.countEditKotlinFile
                ?: return false
        val numberRelevantEditKotlin = editorState.values.fold(0) { acc: Long, editInfo: EditInfo ->
            acc + editInfo.numberEditing
        }
        return numberRelevantEditKotlin > MIN_NUMBER_RELEVANT_EDITING_KOTLIN_FILE
    }

    private fun isKotlinPluginEAP(): Boolean {
        val kotlinPluginDescriptor: IdeaPluginDescriptor = PluginManagerCore.getPlugin(
                PluginId.findId(KOTLIN_PLUGIN_ID)
        ) ?: return false
        return kotlinPluginDescriptor.version.contains("M")
                || kotlinPluginDescriptor.version.contains("eap")
    }

    private fun checkFeedbackDate(): Boolean {
        val dateFeedbackState: DateFeedbackState = service<DateFeedbackStatService>().state ?: return false
        val dayFromLastSendFeedback = LocalDate.now().toEpochDay() - dateFeedbackState.sendFeedbackDate.toEpochDay()
        val dayFromLastShowFeedbackNotification = LocalDate.now().toEpochDay() -
                dateFeedbackState.showFeedbackNotificationDate.toEpochDay()
        return dayFromLastSendFeedback >= MIN_DAYS_SINCE_SEND_FEEDBACK &&
                dayFromLastShowFeedbackNotification >= MIN_DAYS_SINCE_SHOW_FEEDBACK_NOTIFICATION
    }
}