package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.active

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.show.showInactiveTimeNotificationIfPossible
import com.intellij.openapi.project.Project
import java.time.LocalDateTime

/**
 * Temporary storage for information about the last user activity.
 *
 * @see com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.track.active.EditorMouseEventTracker
 * @see com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.track.active.EditorTypingEventTracker
 */

object LastActive {
    var lastActive: LocalDateTime = LocalDateTime.now()

    internal fun trackActive(project: Project) {
        showInactiveTimeNotificationIfPossible(project)
        lastActive = LocalDateTime.now()
    }
}