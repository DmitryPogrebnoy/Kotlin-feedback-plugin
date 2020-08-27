package kotlinFeedbackPlugin.state.services

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import kotlinFeedbackPlugin.state.task.TasksStatisticState

/**
 * Service for working with tasks statistic state.
 *
 * @see kotlinFeedbackPlugin.state.task.TasksStatisticState
 */

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