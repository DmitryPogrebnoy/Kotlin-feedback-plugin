package kotlinFeedbackPlugin.ui.notification

import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup

/**
 * Plugin notification group.
 */

object KotlinFeedbackNotificationGroup {
    val group = NotificationGroup(
            "Kotlin Feedback Notifications",
            NotificationDisplayType.BALLOON,
            true
    )
}