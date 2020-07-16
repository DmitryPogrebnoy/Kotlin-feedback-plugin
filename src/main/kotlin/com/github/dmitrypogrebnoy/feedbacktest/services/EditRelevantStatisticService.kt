package com.github.dmitrypogrebnoy.feedbacktest.services

import com.github.dmitrypogrebnoy.feedbacktest.state.editor.EditRelevantState
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "EditRelevantState", reloadable = true, storages = [Storage("EditRelevantState.xml")])
class EditRelevantStatisticService : PersistentStateComponent<EditRelevantState> {

    private var state: EditRelevantState = EditRelevantState()

    override fun getState(): EditRelevantState? {
        return state
    }

    override fun loadState(stateLoaded: EditRelevantState) {
        state = stateLoaded
    }
}