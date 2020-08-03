package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.show

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.active.LastActive
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.editor.EditInfo
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.DateFeedbackStatService
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.EditStatisticService
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.TasksStatisticService
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.show.DateFeedbackState
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task.*
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.application.ex.ApplicationInfoEx
import com.intellij.openapi.components.service
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.psi.search.FilenameIndex
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime


internal const val KOTLIN_PLUGIN_ID = "org.jetbrains.kotlin"

internal const val EDU_TOOLS_PLUGIN_ID = "com.jetbrains.edu"

// 30 days
internal const val MIN_DAYS_SINCE_SEND_FEEDBACK: Long = 0

// 5 days
internal const val MIN_DAYS_SINCE_SHOW_FEEDBACK_NOTIFICATION: Long = 0

// 5 times
internal const val MIN_NUMBER_RELEVANT_EDITING_KOTLIN_FILE = 0

// 10 days
internal const val RELEVANT_DAYS: Long = 10

// 2 minutes
internal const val MIN_DURATION_COMPILE_TASK = 2

// 2 minutes
internal const val MIN_DURATION_GRADLE_TASK = 0

// 20 minutes
internal const val INACTIVE_TIME: Long = 20

internal fun checkCompileTaskDuration(projectName: String): Boolean {
    val taskStatisticState: TasksStatisticState = service<TasksStatisticService>().state
            ?: return false
    val curProjectTask = ProjectTask(projectName, MAKE_TASK_NAME)
    return if (taskStatisticState.projectsTasksInfo.containsKey(curProjectTask)) {
        val statisticInfo = taskStatisticState.projectsTasksInfo[curProjectTask]!!
        statisticInfo.lastTaskDurationTime > MIN_DURATION_COMPILE_TASK
    } else false
}

internal fun checkGradleExecuteTaskDuration(projectName: String, taskName: String): Boolean {
    val taskStatisticState: TasksStatisticState = service<TasksStatisticService>().state
            ?: return false
    val curProjectTask = ProjectTask(projectName, taskName)
    return if (taskStatisticState.projectsTasksInfo.containsKey(curProjectTask)) {
        val statisticInfo = taskStatisticState.projectsTasksInfo[curProjectTask]!!
        statisticInfo.lastTaskDurationTime > MIN_DURATION_GRADLE_TASK
    } else false
}

internal fun checkGradleResolveTaskDuration(projectName: String): Boolean {
    val taskStatisticState: TasksStatisticState = service<TasksStatisticService>().state
            ?: return false
    val curProjectTask = ProjectTask(projectName, GRADLE_RESOLVE_PROJECT)
    return if (taskStatisticState.projectsTasksInfo.containsKey(curProjectTask)) {
        val statisticInfo = taskStatisticState.projectsTasksInfo[curProjectTask]!!
        statisticInfo.lastTaskDurationTime > MIN_DURATION_GRADLE_TASK
    } else false
}

internal fun checkGradleRefreshTaskDuration(projectName: String): Boolean {
    val taskStatisticState: TasksStatisticState = service<TasksStatisticService>().state
            ?: return false
    val curProjectTask = ProjectTask(projectName, GRADLE_REFRESH_TASKS_LISTS_PROJECT)
    return if (taskStatisticState.projectsTasksInfo.containsKey(curProjectTask)) {
        val statisticInfo = taskStatisticState.projectsTasksInfo[curProjectTask]!!
        statisticInfo.lastTaskDurationTime > MIN_DURATION_GRADLE_TASK
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

internal fun isEduToolsInstalled(): Boolean {
    return PluginManagerCore.getPlugin(PluginId.findId(EDU_TOOLS_PLUGIN_ID)) != null
}

internal fun isEduToolsEnabled(): Boolean {
    val eduToolsPluginDescriptor: IdeaPluginDescriptor = PluginManagerCore.getPlugin(
            PluginId.findId(EDU_TOOLS_PLUGIN_ID)
    ) ?: return false
    return eduToolsPluginDescriptor.isEnabled
}

internal fun checkFeedbackDate(): Boolean {
    val dateFeedbackState: DateFeedbackState = service<DateFeedbackStatService>().state ?: return false
    val dayFromLastSendFeedback = Duration.between(
            LocalDate.now().atStartOfDay(),
            dateFeedbackState.sendFeedbackDate.atStartOfDay()).toDays()
    val dayFromLastShowFeedbackNotification = Duration.between(LocalDate.now().atStartOfDay(),
            dateFeedbackState.showFeedbackNotificationDate.atStartOfDay()).toDays()

    return dayFromLastSendFeedback >= MIN_DAYS_SINCE_SEND_FEEDBACK &&
            dayFromLastShowFeedbackNotification >= MIN_DAYS_SINCE_SHOW_FEEDBACK_NOTIFICATION
}

internal fun checkLastActive(lastActiveDateTime: LocalDateTime): Boolean {
    return Duration.between(LocalDateTime.now(), lastActiveDateTime).toMinutes() >= INACTIVE_TIME
}

internal fun isKotlinProject(project: Project): Boolean {
    return FilenameIndex.getAllFilesByExt(project, "kt").isNotEmpty()
}

internal fun canShowNotificationInCompileTime(project: Project): Boolean {
    return isIntellijIdeaEAP() && checkRelevantNumberEditing() && checkCompileTaskDuration(project.name)
            && isKotlinProject(project) && checkFeedbackDate()
}

internal fun canShowNotificationInGradleExecuteTaskTime(project: Project, taskName: String): Boolean {
    return isIntellijIdeaEAP() && checkRelevantNumberEditing()
            && checkGradleExecuteTaskDuration(project.name, taskName)
            && isKotlinProject(project) && checkFeedbackDate()
}

internal fun canShowNotificationInGradleRefreshTaskTime(project: Project): Boolean {
    return isIntellijIdeaEAP() && checkRelevantNumberEditing()
            && checkGradleRefreshTaskDuration(project.name)
            && isKotlinProject(project) && checkFeedbackDate()
}

internal fun canShowNotificationInGradleResolveTaskTime(project: Project): Boolean {
    return isIntellijIdeaEAP() && checkRelevantNumberEditing()
            && checkGradleResolveTaskDuration(project.name)
            && isKotlinProject(project) && checkFeedbackDate()
}

internal fun canShowNotificationInInactiveTime(project: Project): Boolean {
    return isIntellijIdeaEAP() && checkRelevantNumberEditing() && checkLastActive(LastActive.lastActive)
            && isKotlinProject(project) && checkFeedbackDate()
}