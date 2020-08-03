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

    private val feedbackNotification = Notification(
            KotlinFeedbackNotificationGroup.group.displayId,
            message("request.feedback.notification.title"),
            message("request.feedback.notification.content"),
            NotificationType.INFORMATION
    ).addAction(
            NotificationAction.createSimple(message("request.feedback.notification.action.submit")) {
                val dialog = FeedbackDialog(project)
                dialog.show()
            }
    )

    fun trackingNotify() {
        val dateFeedbackState: DateFeedbackState = service<DateFeedbackStatService>().state ?: return
        feedbackNotification.notify(project)
        dateFeedbackState.showFeedbackNotificationDate = LocalDate.now()
    }
}