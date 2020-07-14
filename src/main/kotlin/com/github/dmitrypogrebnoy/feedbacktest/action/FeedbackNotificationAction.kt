package com.github.dmitrypogrebnoy.feedbacktest.action

import com.github.dmitrypogrebnoy.feedbacktest.notification.FeedbackNotification
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent


class FeedbackNotificationAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        FeedbackNotification(e.project!!).notificationNotify()
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }
}
