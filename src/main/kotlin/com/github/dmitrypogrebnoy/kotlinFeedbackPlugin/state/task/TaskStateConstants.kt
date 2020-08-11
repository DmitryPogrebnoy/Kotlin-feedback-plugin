package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task

/**
 * Constants for correctly tracking the duration of Gradle and IDEA tasks.
 */

const val TASK_DEFAULT_START_TIME: Int = 0
const val MAKE_TASK_NAME: String = "_MAKE"
val GRADLE_TASKS_FOR_TRACK: List<String> = listOf("build", "test", "compileKotlin", "compileTestKotlin")
const val GRADLE_RESOLVE_PROJECT = "_GRADLE_RESOLVE_PROJECT"
const val GRADLE_REFRESH_TASKS_LISTS_PROJECT = "_GRADLE_REFRESH_TASK_LISTS_PROJECT"
const val GRADLE_PROJECT_SYSTEM_ID = "GRADLE"