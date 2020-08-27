package kotlinFeedbackPlugin.state.services

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import kotlinFeedbackPlugin.state.show.FeedbackDatesState

/**
 * Service for working with feedback dates state.
 *
 * @see kotlinFeedbackPlugin.state.show.FeedbackDatesState
 */

@State(name = "FeedbackDatesState", reloadable = true, storages = [Storage("FeedbackDatesState.xml")])
class FeedbackDatesService : PersistentStateComponent<FeedbackDatesState> {

    private var state: FeedbackDatesState = FeedbackDatesState()

    override fun getState(): FeedbackDatesState? {
        return state
    }

    override fun loadState(loadedState: FeedbackDatesState) {
        state = loadedState
    }
}