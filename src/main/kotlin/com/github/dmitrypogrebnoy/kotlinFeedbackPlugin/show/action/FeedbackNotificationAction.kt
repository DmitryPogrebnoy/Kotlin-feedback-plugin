package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.show.action

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification.FeedbackNotification
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent


class FeedbackNotificationAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        FeedbackNotification.showNotification(e.project!!)
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }
}
