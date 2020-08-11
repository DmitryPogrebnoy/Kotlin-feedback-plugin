package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task

/**
 * Represents information about the Gradle task.
 */

data class GradleProjectTask(
        val projectName: String,
        val taskId: String
)