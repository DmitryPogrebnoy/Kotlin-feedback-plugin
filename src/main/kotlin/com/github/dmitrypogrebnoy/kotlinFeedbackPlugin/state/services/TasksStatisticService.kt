package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task.TasksStatisticState
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

/**
 * Service for working with tasks statistic state.
 *
 * @see com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task.TasksStatisticState
 */

@State(name = "TasksStatisticState", reloadable = true, storages = [Storage("TasksStatisticState.xml")])
class TasksStatisticService : PersistentStateComponent<TasksStatisticState> {

    private var state: TasksStatisticState = TasksStatisticState()

    override fun getState(): TasksStatisticState? {
        return state
    }

    override fun loadState(stateLoaded: TasksStatisticState) {
        state = stateLoaded
    }
}