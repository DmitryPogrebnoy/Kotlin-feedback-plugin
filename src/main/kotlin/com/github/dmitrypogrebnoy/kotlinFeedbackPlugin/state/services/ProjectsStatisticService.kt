package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.project.ProjectsStatisticState
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

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