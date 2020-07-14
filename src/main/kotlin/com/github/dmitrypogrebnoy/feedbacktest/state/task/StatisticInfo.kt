package com.github.dmitrypogrebnoy.feedbacktest.state.task

data class StatisticInfo(
        val lastTaskDurationTime: Int = TASK_DEFAULT_START_TIME,
        val startTaskTime: Int = TASK_DEFAULT_DURATION_TIME)