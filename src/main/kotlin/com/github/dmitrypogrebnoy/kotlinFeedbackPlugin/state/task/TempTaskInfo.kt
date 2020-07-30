package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task

object TempTaskInfo {
    val ideaTasksStartTime: MutableMap<IDEAProjectTask, Int> = mutableMapOf()
    val gradleExecuteTasksStartInfo: MutableMap<GradleProjectTask, GradleTaskTempInfo> = mutableMapOf()

    // Map<project name, start time in seconds>
    val gradleResolveProjectTasksStartTime: MutableMap<String, Int> = mutableMapOf()

    // Map<project name, start time in seconds>
    val gradleRefreshTaskListsTasksStartTime: MutableMap<String, Int> = mutableMapOf()
}