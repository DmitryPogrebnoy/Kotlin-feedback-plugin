package com.github.dmitrypogrebnoy.feedbacktest.action

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class HotKeyAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        Notifications.Bus.notify(
                Notification(
                        "ProjectOpenNotification",
                        "Project is OPEN",
                        " Project name is ${e.project?.name}",
                        NotificationType.INFORMATION
                )
        )
    }
}
