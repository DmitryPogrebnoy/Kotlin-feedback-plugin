package kotlinFeedbackPlugin.state.services

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import kotlinFeedbackPlugin.state.project.ProjectsStatisticState

/**
 * Service for working with projects statistic state.
 *
 * @see kotlinFeedbackPlugin.state.project.ProjectsStatisticState
 */

@State(name = "ProjectsStatisticState", reloadable = true, storages = [Storage("ProjectsStatisticState.xml")])
class ProjectsStatisticService : PersistentStateComponent<ProjectsStatisticState> {

    private var state: ProjectsStatisticState = ProjectsStatisticState()

    override fun getState(): ProjectsStatisticState? {
        return state
    }

    override fun loadState(loadedState: ProjectsStatisticState) {
        state = loadedState
    }
}