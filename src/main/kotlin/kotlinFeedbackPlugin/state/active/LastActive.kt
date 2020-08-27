package kotlinFeedbackPlugin.state.active

import com.intellij.openapi.project.Project
import kotlinFeedbackPlugin.show.showInactiveTimeNotificationIfPossible
import java.time.LocalDateTime

/**
 * Temporary storage for information about the last user activity.
 *
 * @see kotlinFeedbackPlugin.track.active.EditorMouseEventTracker
 * @see kotlinFeedbackPlugin.track.active.EditorTypingEventTracker
 */

object LastActive {
    var lastActive: LocalDateTime = LocalDateTime.now()

    internal fun trackActive(project: Project) {
        showInactiveTimeNotificationIfPossible(project)
        lastActive = LocalDateTime.now()
    }
}