package com.github.dmitrypogrebnoy.feedbacktest.services

import com.github.dmitrypogrebnoy.feedbacktest.state.task.TasksStatisticState
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

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