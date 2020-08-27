package kotlinFeedbackPlugin.state.services

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import kotlinFeedbackPlugin.state.editor.EditState

/**
 * Service for working with edit state.
 *
 * @see kotlinFeedbackPlugin.state.editor.EditState
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