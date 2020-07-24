package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task.converter.TasksStatisticConverter
import com.intellij.util.xmlb.annotations.OptionTag

data class TasksStatisticState(
        @OptionTag(converter = TasksStatisticConverter::class)
        val projectsTasksInfo: MutableMap<ProjectTask, TaskStatisticInfo> = mutableMapOf())