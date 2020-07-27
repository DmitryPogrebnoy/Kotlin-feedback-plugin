package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.track.active

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.show.checkLastActive
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.active.LastActive
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification.FeedbackNotification
import com.intellij.openapi.project.Project
import java.time.LocalDateTime

internal fun trackActive(project: Project) {
    if (checkLastActive(LastActive.lastActive)) {
        FeedbackNotification.showNotification(project)
    }
    LastActive.lastActive = LocalDateTime.now()
}