package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.bundle.FeedbackBundle
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType

class SuccessSendFeedbackNotification(
        titleText: String = FeedbackBundle.message("success.send.feedback.default.notification.title"),
        descriptionText: String = FeedbackBundle.message("success.send.feedback.default.notification.content")
) : Notification(
        KotlinFeedbackNotificationGroup.group.displayId,
        titleText,
        descriptionText,
        NotificationType.INFORMATION
)