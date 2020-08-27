package kotlinFeedbackPlugin.ui.notification

import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent
import kotlinFeedbackPlugin.bundle.FeedbackBundle
import kotlinFeedbackPlugin.user.LostUserType

/**
 * Notification action for lost user type.
 * When executed, it opens a feedback dialog for the lost user type.
 */

class LostUserTypeNotificationAction : NotificationAction(
        FeedbackBundle.message("request.feedback.default.notification.action.submit")
) {
    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
        LostUserType.showFeedbackDialog(e.project ?: return)
    }
}
