package kotlinFeedbackPlugin.ui.notification

import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent
import kotlinFeedbackPlugin.bundle.FeedbackBundle
import kotlinFeedbackPlugin.user.BeginnerUserType

/**
 * Notification action for beginner user type.
 * When executed, it opens a feedback dialog for the beginner user type.
 */

class BeginnerUserTypeNotificationAction : NotificationAction(
        FeedbackBundle.message("request.feedback.default.notification.action.submit")
) {
    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
        BeginnerUserType.showFeedbackDialog(e.project ?: return)
    }
}
