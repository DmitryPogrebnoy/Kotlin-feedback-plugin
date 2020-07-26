package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.editor.EditState
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "EditRelevantState", reloadable = true, storages = [Storage("EditRelevantState.xml")])
class EditStatisticService : PersistentStateComponent<EditState> {

    private var state: EditState = EditState()

    override fun getState(): EditState? {
        return state
    }

    override fun loadState(stateLoaded: EditState) {
        state = stateLoaded
    }
}