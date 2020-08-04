package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.track.project

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.show.hasVcs
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.show.isKotlinProject
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.project.ProjectState
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.project.ProjectsStatisticState
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.ProjectsStatisticService
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import java.time.LocalDate

class TrackOpenedProjectInfo : ProjectManagerListener {

    private val projectsStatisticsService: ProjectsStatisticService = service()

    override fun projectClosing(project: Project) {
        updateProjectStatisticState(project)
    }

    //TODO: Fix this
    //projectOpened doesn't work with isKotlinProject() if project indexing is not complete
    override fun projectOpened(project: Project) {
        updateProjectStatisticState(project)
    }

    private fun updateProjectStatisticState(project: Project) {
        val state: ProjectsStatisticState = projectsStatisticsService.state ?: return
        state.projectsStatisticState[project.name] = ProjectState(
                LocalDate.now(),
                hasVcs(project),
                isKotlinProject(project)
        )
    }

}