package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.track.active

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.show.showInactiveTimeNotificationIfPossible
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.active.LastActive
import com.intellij.openapi.project.Project
import java.time.LocalDateTime

internal fun trackActive(project: Project) {
    showInactiveTimeNotificationIfPossible(project)
    LastActive.lastActive = LocalDateTime.now()
}