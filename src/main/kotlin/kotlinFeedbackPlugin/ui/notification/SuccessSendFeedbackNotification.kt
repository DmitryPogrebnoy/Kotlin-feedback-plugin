package kotlinFeedbackPlugin.ui.notification

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.util.IconLoader
import kotlinFeedbackPlugin.bundle.FeedbackBundle

/**
 * Basic notification to thank the user for the Kotlin feedback
 */

class SuccessSendFeedbackNotification(
        titleText: String = FeedbackBundle.message("success.send.feedback.default.notification.title"),
        descriptionText: String = FeedbackBundle.message("success.send.feedback.default.notification.content")
) : Notification(
        KotlinFeedbackNotificationGroup.group.displayId,
        //TODO: Set right icon
        IconLoader.getIcon("/kotlin.svg"),
        titleText,
        "",
        descriptionText,
        NotificationType.INFORMATION,
        null
)