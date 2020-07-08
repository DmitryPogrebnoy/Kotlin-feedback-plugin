package com.github.dmitrypogrebnoy.feedbacktest.services

import com.github.dmitrypogrebnoy.feedbacktest.state.StatisticState
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "StatisticState", reloadable = true, storages = [Storage("StatisticState.xml")])
class StatisticPersistentStateService : PersistentStateComponent<StatisticState> {

    private var state: StatisticState = StatisticState()

    override fun getState(): StatisticState? {
        return state
    }

    override fun loadState(stateLoaded: StatisticState) {
        state = stateLoaded
    }
}