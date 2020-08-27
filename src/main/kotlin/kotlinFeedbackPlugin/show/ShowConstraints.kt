package kotlinFeedbackPlugin.show

import com.intellij.openapi.application.ex.ApplicationInfoEx
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import kotlinFeedbackPlugin.network.ShowConstraintsConstantsLoader.getMinDaysSinceCloseFeedbackDialog
import kotlinFeedbackPlugin.network.ShowConstraintsConstantsLoader.getMinDaysSinceSendFeedback
import kotlinFeedbackPlugin.network.ShowConstraintsConstantsLoader.getMinDaysSinceShowNotification
import kotlinFeedbackPlugin.network.ShowConstraintsConstantsLoader.getMinDurationCompileTask
import kotlinFeedbackPlugin.network.ShowConstraintsConstantsLoader.getMinDurationGradleTask
import kotlinFeedbackPlugin.network.ShowConstraintsConstantsLoader.getMinInactiveTime
import kotlinFeedbackPlugin.state.active.LastActive
import kotlinFeedbackPlugin.state.services.FeedbackDatesService
import kotlinFeedbackPlugin.state.services.TasksStatisticService
import kotlinFeedbackPlugin.state.show.FeedbackDatesState
import kotlinFeedbackPlugin.state.task.*
import kotlinFeedbackPlugin.user.UserTypeResolver
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Contains constants and functions that implement restrictions on displaying notifications.
 */

// 45 days
private const val DEFAULT_MIN_DAYS_SINCE_SEND_FEEDBACK = 45

// 60 days
private const val DEFAULT_MIN_DAYS_SINCE_CLOSE_FEEDBACK_DIALOG = 60

// 15 days
private const val DEFAULT_MIN_DAYS_SINCE_SHOW_FEEDBACK_NOTIFICATION = 15

// 2 minutes
private const val DEFAULT_MIN_DURATION_COMPILE_TASK = 2

// 2 minutes
private const val DEFAULT_MIN_DURATION_GRADLE_TASK = 2

// 20 minutes
private const val DEFAULT_MIN_INACTIVE_TIME = 20


internal val MIN_DAYS_SINCE_SEND_FEEDBACK = getMinDaysSinceSendFeedback() ?: DEFAULT_MIN_DAYS_SINCE_SEND_FEEDBACK

internal val MIN_DAYS_SINCE_CLOSE_FEEDBACK_DIALOG = getMinDaysSinceCloseFeedbackDialog()
        ?: DEFAULT_MIN_DAYS_SINCE_CLOSE_FEEDBACK_DIALOG

internal val MIN_DAYS_SINCE_SHOW_FEEDBACK_NOTIFICATION = getMinDaysSinceShowNotification()
        ?: DEFAULT_MIN_DAYS_SINCE_SHOW_FEEDBACK_NOTIFICATION

internal val MIN_DURATION_COMPILE_TASK = getMinDurationCompileTask() ?: DEFAULT_MIN_DURATION_COMPILE_TASK

internal val MIN_DURATION_GRADLE_TASK = getMinDurationGradleTask() ?: DEFAULT_MIN_DURATION_GRADLE_TASK

internal val MIN_INACTIVE_TIME = getMinInactiveTime() ?: DEFAULT_MIN_INACTIVE_TIME


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

internal fun checkFeedbackDatesForNotifications(): Boolean {
    val feedbackDatesState: FeedbackDatesState = service<FeedbackDatesService>().state ?: return false
    val dayFromLastSendFeedback = Duration.between(
            feedbackDatesState.dateSendFeedback.atStartOfDay(),
            LocalDate.now().atStartOfDay()).toDays()
    val dayFromLastCloseFeedbackDialog = Duration.between(
            feedbackDatesState.dateCloseFeedbackDialog.atStartOfDay(),
            LocalDate.now().atStartOfDay()).toDays()
    val dayFromLastShowFeedbackNotification = Duration.between(
            feedbackDatesState.dateShowFeedbackNotification.atStartOfDay(),
            LocalDate.now().atStartOfDay()).toDays()
    return dayFromLastSendFeedback >= MIN_DAYS_SINCE_SEND_FEEDBACK
            && dayFromLastCloseFeedbackDialog >= MIN_DAYS_SINCE_CLOSE_FEEDBACK_DIALOG
            && dayFromLastShowFeedbackNotification >= MIN_DAYS_SINCE_SHOW_FEEDBACK_NOTIFICATION
}

internal fun checkFeedbackDatesForWidget(): Boolean {
    val feedbackDatesState: FeedbackDatesState = service<FeedbackDatesService>().state ?: return false
    val dayFromLastSendFeedback = Duration.between(
            feedbackDatesState.dateSendFeedback.atStartOfDay(),
            LocalDate.now().atStartOfDay()).toDays()
    val dayFromLastCloseFeedbackDialog = Duration.between(
            feedbackDatesState.dateCloseFeedbackDialog.atStartOfDay(),
            LocalDate.now().atStartOfDay()).toDays()
    return dayFromLastSendFeedback >= MIN_DAYS_SINCE_SEND_FEEDBACK
            && dayFromLastCloseFeedbackDialog >= MIN_DAYS_SINCE_CLOSE_FEEDBACK_DIALOG
}

internal fun checkLastActive(lastActiveDateTime: LocalDateTime): Boolean {
    return Duration.between(LocalDateTime.now(), lastActiveDateTime).toMinutes() >= MIN_INACTIVE_TIME
}

internal fun isIntellijIdeaEAP(): Boolean {
    return ApplicationInfoEx.getInstanceEx().isEAP
}

internal fun showCompileTimeNotificationIfPossible(project: Project) {
    if (checkCompileTaskDuration(project.name) && checkFeedbackDatesForNotifications()) {
        UserTypeResolver.showFeedbackNotification(project)
    }
}

internal fun showGradleExecuteTaskTimeNotificationIfPossible(project: Project, taskName: String) {
    if (checkGradleExecuteTaskDuration(project.name, taskName) && checkFeedbackDatesForNotifications()) {
        UserTypeResolver.showFeedbackNotification(project)
    }
}

internal fun showGradleRefreshTaskTimeNotificationIfPossible(project: Project) {
    if (checkGradleRefreshTaskDuration(project.name) && checkFeedbackDatesForNotifications()) {
        UserTypeResolver.showFeedbackNotification(project)
    }
}

internal fun showGradleResolveTaskTimeNotificationIfPossible(project: Project) {
    if (checkGradleResolveTaskDuration(project.name) && checkFeedbackDatesForNotifications()) {
        UserTypeResolver.showFeedbackNotification(project)
    }
}

internal fun showInactiveTimeNotificationIfPossible(project: Project) {
    if (checkLastActive(LastActive.lastActive) && checkFeedbackDatesForNotifications()) {
        UserTypeResolver.showFeedbackNotification(project)
    }
}