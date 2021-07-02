package kotlinFeedbackPlugin.ui.notification

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class TestLaunchNotification : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val notification = generateRequestFeedbackNotification()
        notification.addAction(BeginnerUserTypeNotificationAction())
        notification.addAction(DontShowFeedbackNotificationAction())
        notification.notify(e.project)
    }
}