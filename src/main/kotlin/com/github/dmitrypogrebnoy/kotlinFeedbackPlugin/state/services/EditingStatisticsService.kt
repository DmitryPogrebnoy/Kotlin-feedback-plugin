package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.editor.EditState
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

/**
 * Service for working with edit state.
 *
 * @see com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.editor.EditState
 */

@State(name = "EditState", reloadable = true, storages = [Storage("EditState.xml")])
class EditingStatisticsService : PersistentStateComponent<EditState> {

    private var state: EditState = EditState()

    override fun getState(): EditState? {
        return state
    }

    override fun loadState(stateLoaded: EditState) {
        state = stateLoaded
    }
}