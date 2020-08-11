package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task

/**
 * Represents temporary information about the Gradle task.
 */

data class GradleTaskTempInfo(
        val startTime: Int = 0,
        val taskName: String
)