package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.bundle.FeedbackBundle.message
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.DateFeedbackStatService
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.show.DateFeedbackState
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.dialog.FeedbackDialog
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import java.time.LocalDate

class FeedbackNotification(private val project: Project) {

    companion object {
        internal fun showNotification(project: Project) {
            val dateFeedbackState: DateFeedbackState = service<DateFeedbackStatService>().state ?: return
            FeedbackNotification(project).notificationNotify()
            dateFeedbackState.showFeedbackNotificationDate = LocalDate.now()
        }
    }

    private val feedbackNotification = Notification(
            "Feedback Notification",
            message("notification.title"),
            message("notification.content"),
            NotificationType.INFORMATION
    ).addAction(
            NotificationAction.createSimple(message("notification.action.submit")) {
                val dialog = FeedbackDialog(project)
                dialog.show()
            }
    )

    private fun notificationNotify() {
        feedbackNotification.notify(project)
    }
}