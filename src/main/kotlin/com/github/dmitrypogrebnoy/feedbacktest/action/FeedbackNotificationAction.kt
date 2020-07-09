package com.github.dmitrypogrebnoy.feedbacktest.action

import com.github.dmitrypogrebnoy.feedbacktest.dialog.FeedbackDialog
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent


class FeedbackNotificationAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val notification: Notification = Notification(
                "Feedback notification",
                "Help make it better",
                " Tell us about your experience.",
                NotificationType.INFORMATION
        ).addAction(
                NotificationAction.createSimple("Give feedback") {
                    val dialog = FeedbackDialog(e.project!!)
                    dialog.show()
                }
        )

        notification.notify(e.project)
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }
}
