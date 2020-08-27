package kotlinFeedbackPlugin.ui.notification

import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent
import kotlinFeedbackPlugin.bundle.FeedbackBundle
import kotlinFeedbackPlugin.user.ActiveUserType

/**
 * Notification action for active user type.
 * When executed, it opens a feedback dialog for the active user type.
 */

class ActiveUserTypeNotificationAction : NotificationAction(
        FeedbackBundle.message("request.feedback.default.notification.action.submit")
) {
    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
        ActiveUserType.showFeedbackDialog(e.project ?: return)
    }
}