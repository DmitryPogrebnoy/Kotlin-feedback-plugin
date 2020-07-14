package com.github.dmitrypogrebnoy.feedbacktest.state.task

import com.github.dmitrypogrebnoy.feedbacktest.state.task.converter.TasksStatisticConverter
import com.intellij.util.xmlb.annotations.OptionTag

data class TasksStatisticState(
        @OptionTag(converter = TasksStatisticConverter::class)
        val projectsTasksInfo: MutableMap<ProjectTask, StatisticInfo> = mutableMapOf())