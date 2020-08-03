package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent


class FeedbackNotificationAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        FeedbackNotification(e.project!!).trackingNotify()
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }
}
