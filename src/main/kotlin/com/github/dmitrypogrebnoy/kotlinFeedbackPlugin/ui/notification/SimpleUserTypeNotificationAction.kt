package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.bundle.FeedbackBundle
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user.SimpleUserType
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent

class SimpleUserTypeNotificationAction : NotificationAction(
        FeedbackBundle.message("request.feedback.default.notification.action.submit")
) {
    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
        SimpleUserType.showFeedbackDialog(e.project ?: return)
    }
}