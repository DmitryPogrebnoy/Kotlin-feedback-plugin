package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.active

import java.time.LocalDateTime

object LastActive {
    var lastActive: LocalDateTime = LocalDateTime.now()
}