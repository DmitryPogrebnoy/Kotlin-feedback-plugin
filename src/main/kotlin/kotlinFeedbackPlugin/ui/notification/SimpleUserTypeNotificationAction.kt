package kotlinFeedbackPlugin.ui.notification

import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent
import kotlinFeedbackPlugin.bundle.FeedbackBundle
import kotlinFeedbackPlugin.user.SimpleUserType

/**
 * Notification action for simple user type.
 * When executed, it opens a feedback dialog for the simple user type.
 */

class SimpleUserTypeNotificationAction : NotificationAction(
        FeedbackBundle.message("request.feedback.default.notification.action.submit")
) {
    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
        SimpleUserType.showFeedbackDialog(e.project ?: return)
    }
}