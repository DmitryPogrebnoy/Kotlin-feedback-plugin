package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.bundle.FeedbackBundle
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

class SuccessSendFeedbackNotification(private val project: Project?) {
    private val successSendFeedbackNotification = Notification(
            KotlinFeedbackNotificationGroup.group.displayId,
            FeedbackBundle.message("success.send.feedback.notification.title"),
            FeedbackBundle.message("success.send.feedback.notification.content"),
            NotificationType.INFORMATION
    )

    fun justNotify() {
        successSendFeedbackNotification.notify(project)
    }
}