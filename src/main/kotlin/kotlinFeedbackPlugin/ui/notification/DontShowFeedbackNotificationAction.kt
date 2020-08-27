package kotlinFeedbackPlugin.ui.notification

import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import kotlinFeedbackPlugin.bundle.FeedbackBundle
import kotlinFeedbackPlugin.state.services.FeedbackDatesService
import java.time.LocalDate

/**
 * Notification action for disable showing request feedback notification.
 */

class DontShowFeedbackNotificationAction : NotificationAction(
        FeedbackBundle.message("request.feedback.default.notification.action.not.show")
) {
    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
        val feedbackDatesState = service<FeedbackDatesService>().state ?: return
        feedbackDatesState.dateShowFeedbackNotification = LocalDate.MAX
        notification.expire()
    }
}