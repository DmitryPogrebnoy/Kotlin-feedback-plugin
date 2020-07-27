package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.show

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.editor.EditInfo
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.DateFeedbackStatService
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.EditStatisticService
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.TasksStatisticService
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.show.DateFeedbackState
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task.MAKE_TASK_NAME
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task.ProjectTask
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task.TasksStatisticState
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.application.ex.ApplicationInfoEx
import com.intellij.openapi.components.service
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import java.time.LocalDate
import java.time.LocalDateTime


internal const val KOTLIN_PLUGIN_ID = "org.jetbrains.kotlin"

// 30 days
internal const val MIN_DAYS_SINCE_SEND_FEEDBACK: Long = 30

// 5 days
internal const val MIN_DAYS_SINCE_SHOW_FEEDBACK_NOTIFICATION: Long = 5

// 5 times
internal const val MIN_NUMBER_RELEVANT_EDITING_KOTLIN_FILE = 5

// 10 days
internal const val RELEVANT_DAYS: Long = 10

// 2 minutes
internal const val MIN_DURATION_COMPILE_TASK = 2

// 20 minutes
internal val INACTIVE_TIME: LocalDateTime = LocalDateTime.of(0, 0, 0, 0, 20)

internal fun checkCompileTaskDuration(project: Project): Boolean {
    val taskStatisticState: TasksStatisticState = service<TasksStatisticService>().state
            ?: return false
    val curProjectTask = ProjectTask(project.name, MAKE_TASK_NAME)
    return if (taskStatisticState.projectsTasksInfo.containsKey(curProjectTask)) {
        val statisticInfo = taskStatisticState.projectsTasksInfo[curProjectTask]!!
        statisticInfo.lastTaskDurationTime > MIN_DURATION_COMPILE_TASK
    } else false
}

internal fun checkRelevantNumberEditing(): Boolean {
    val editorState: Map<LocalDate, EditInfo> = service<EditStatisticService>().state?.countEditKotlinFile
            ?: return false
    val startRelevantDays = LocalDate.now().minusDays(RELEVANT_DAYS)
    val numberRelevantEditKotlin = editorState.entries.fold(0) { acc: Long, entry: Map.Entry<LocalDate, EditInfo> ->
        if (entry.key >= startRelevantDays && entry.key <= LocalDate.now()) {
            acc + entry.value.numberEditing
        } else acc
    }
    return numberRelevantEditKotlin > MIN_NUMBER_RELEVANT_EDITING_KOTLIN_FILE
}

internal fun isIntellijIdeaEAP(): Boolean {
    return ApplicationInfoEx.getInstanceEx().isEAP
}

internal fun isKotlinPluginEAP(): Boolean {
    val kotlinPluginDescriptor: IdeaPluginDescriptor = PluginManagerCore.getPlugin(
            PluginId.findId(KOTLIN_PLUGIN_ID)
    ) ?: return false
    return kotlinPluginDescriptor.version.contains("M")
            || kotlinPluginDescriptor.version.contains("eap")
}

internal fun checkFeedbackDate(): Boolean {
    val dateFeedbackState: DateFeedbackState = service<DateFeedbackStatService>().state ?: return false
    val dayFromLastSendFeedback = LocalDate.now().toEpochDay() - dateFeedbackState.sendFeedbackDate.toEpochDay()
    val dayFromLastShowFeedbackNotification = LocalDate.now().toEpochDay() -
            dateFeedbackState.showFeedbackNotificationDate.toEpochDay()
    return dayFromLastSendFeedback >= MIN_DAYS_SINCE_SEND_FEEDBACK &&
            dayFromLastShowFeedbackNotification >= MIN_DAYS_SINCE_SHOW_FEEDBACK_NOTIFICATION
}

internal fun checkLastActive(lastActiveDateTime: LocalDateTime): Boolean {
    val timeDifference = LocalDateTime.now()
    timeDifference.minusDays(lastActiveDateTime.dayOfYear.toLong())
    timeDifference.minusHours(lastActiveDateTime.hour.toLong())
    timeDifference.minusMinutes(lastActiveDateTime.minute.toLong())
    timeDifference.minusSeconds(lastActiveDateTime.second.toLong())
    timeDifference.minusNanos(lastActiveDateTime.nano.toLong())
    return timeDifference > INACTIVE_TIME
}