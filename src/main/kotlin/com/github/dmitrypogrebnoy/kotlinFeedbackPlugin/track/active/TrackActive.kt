package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.track.active

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.active.LastActive
import java.time.LocalDateTime

internal fun trackActive() {
    LastActive.lastActive = LocalDateTime.now()
}