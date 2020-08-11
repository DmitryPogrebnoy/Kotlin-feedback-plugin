package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task

/**
 * Represents information about the IDEA project task.
 */

data class IDEAProjectTask(
        val projectName: String,
        val taskName: String
)