package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.bundle.FeedbackBundle
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user.BeginnerUserType
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent

class BeginnerUserTypeNotificationAction : NotificationAction(
        FeedbackBundle.message("request.feedback.default.notification.action.submit")
) {
    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
        BeginnerUserType.showFeedbackDialog(e.project ?: return)
    }
}
