package com.github.dmitrypogrebnoy.feedbacktest.notification

import com.github.dmitrypogrebnoy.feedbacktest.dialog.FeedbackDialog
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

class FeedbackNotification(private val project: Project) {

    private val feedbackNotification = Notification(
            "Feedback Notification",
            "Help make Kotlin plugin better",
            "Tell us about your experience.",
            NotificationType.INFORMATION
    ).addAction(
            NotificationAction.createSimple("Submit Feedback") {
                val dialog = FeedbackDialog(project)
                dialog.show()
            }
    )

    fun notificationNotify() {
        feedbackNotification.notify(project)
    }
}