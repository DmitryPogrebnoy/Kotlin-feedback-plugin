package com.github.dmitrypogrebnoy.feedbacktest.services

import com.github.dmitrypogrebnoy.feedbacktest.state.editor.EditorState
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "EditorState", reloadable = true, storages = [Storage("EditorState.xml")])
class EditorStatisticService : PersistentStateComponent<EditorState> {

    private var state: EditorState = EditorState()

    override fun getState(): EditorState? {
        return state
    }

    override fun loadState(stateLoaded: EditorState) {
        state = stateLoaded
    }
}