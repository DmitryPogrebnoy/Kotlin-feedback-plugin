package com.github.dmitrypogrebnoy.feedbacktest.services

import com.github.dmitrypogrebnoy.feedbacktest.state.editor.EditOldState
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "EditOldState", reloadable = true, storages = [Storage("EditOldState.xml")])
class EditOldStatisticService : PersistentStateComponent<EditOldState> {

    private var state: EditOldState = EditOldState()

    override fun getState(): EditOldState? {
        return state
    }

    override fun loadState(loadedState: EditOldState) {
        state = loadedState
    }
}