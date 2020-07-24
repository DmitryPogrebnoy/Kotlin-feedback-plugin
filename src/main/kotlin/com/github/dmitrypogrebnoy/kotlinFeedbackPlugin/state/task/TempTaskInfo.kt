package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task

object TempTaskInfo {
    val taskStartTime: MutableMap<ProjectTask, Int> = mutableMapOf()
}