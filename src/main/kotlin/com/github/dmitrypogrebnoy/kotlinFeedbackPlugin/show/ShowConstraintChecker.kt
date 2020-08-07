package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.show

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.active.LastActive
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.DateFeedbackStatService
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.TasksStatisticService
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.show.DateFeedbackState
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task.*
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user.UserTypeResolver
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

// 45 days
internal const val MIN_DAYS_SINCE_SEND_FEEDBACK: Long = 0

// 90 days
internal const val MIN_DAYS_SINCE_CLOSE_FEEDBACK: Long = 2

// 5 days
internal const val MIN_DAYS_SINCE_SHOW_FEEDBACK_NOTIFICATION: Long = 2

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

internal fun checkFeedbackDate(): Boolean {
    val dateFeedbackState: DateFeedbackState = service<DateFeedbackStatService>().state ?: return false
    val dayFromLastSendFeedback = Duration.between(
            dateFeedbackState.dateSendFeedback.atStartOfDay(),
            LocalDate.now().atStartOfDay()).toDays()
    val dayFromLastCloseFeedbackDialog = Duration.between(
            dateFeedbackState.dateCloseFeedbackDialog.atStartOfDay(),
            LocalDate.now().atStartOfDay()).toDays()
    val dayFromLastShowFeedbackNotification = Duration.between(
            dateFeedbackState.dateShowFeedbackNotification.atStartOfDay(),
            LocalDate.now().atStartOfDay()).toDays()
    return dayFromLastSendFeedback >= MIN_DAYS_SINCE_SEND_FEEDBACK
            && dayFromLastCloseFeedbackDialog >= MIN_DAYS_SINCE_CLOSE_FEEDBACK
            && dayFromLastShowFeedbackNotification >= MIN_DAYS_SINCE_SHOW_FEEDBACK_NOTIFICATION
}

internal fun checkLastActive(lastActiveDateTime: LocalDateTime): Boolean {
    return Duration.between(LocalDateTime.now(), lastActiveDateTime).toMinutes() >= INACTIVE_TIME
}

internal fun showCompileTimeNotificationIfPossible(project: Project) {
    if (checkCompileTaskDuration(project.name) && checkFeedbackDate()) {
        UserTypeResolver.showFeedbackNotification(project)
    }
}

internal fun showGradleExecuteTaskTimeNotificationIfPossible(project: Project, taskName: String) {
    if (checkGradleExecuteTaskDuration(project.name, taskName) && checkFeedbackDate()) {
        UserTypeResolver.showFeedbackNotification(project)
    }
}

internal fun showGradleRefreshTaskTimeNotificationIfPossible(project: Project) {
    if (checkGradleRefreshTaskDuration(project.name) && checkFeedbackDate()) {
        UserTypeResolver.showFeedbackNotification(project)
    }
}

internal fun showGradleResolveTaskTimeNotificationIfPossible(project: Project) {
    if (checkGradleResolveTaskDuration(project.name) && checkFeedbackDate()) {
        UserTypeResolver.showFeedbackNotification(project)
    }
}

internal fun showInactiveTimeNotificationIfPossible(project: Project) {
    if (checkLastActive(LastActive.lastActive) && checkFeedbackDate()) {
        UserTypeResolver.showFeedbackNotification(project)
    }
}