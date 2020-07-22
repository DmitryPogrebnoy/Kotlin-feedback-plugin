package com.github.dmitrypogrebnoy.feedbacktest.services

import com.github.dmitrypogrebnoy.feedbacktest.state.show.notificationFeedback.DateFeedbackState
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "DateFeedbackState", reloadable = true, storages = [Storage("DateFeedbackState.xml")])
class DateFeedbackStatService : PersistentStateComponent<DateFeedbackState> {

    private var state: DateFeedbackState = DateFeedbackState()

    override fun getState(): DateFeedbackState? {
        return state
    }

    override fun loadState(loadedState: DateFeedbackState) {
        state = loadedState
    }
}