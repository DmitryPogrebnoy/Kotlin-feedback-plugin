package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.track.active

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.show.canShowNotificationInInactiveTime
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.active.LastActive
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification.FeedbackNotification
import com.intellij.openapi.project.Project
import java.time.LocalDateTime

internal fun trackActive(project: Project) {
    if (canShowNotificationInInactiveTime(project)) {
        FeedbackNotification(project).notificationNotify()
    }
    LastActive.lastActive = LocalDateTime.now()
}