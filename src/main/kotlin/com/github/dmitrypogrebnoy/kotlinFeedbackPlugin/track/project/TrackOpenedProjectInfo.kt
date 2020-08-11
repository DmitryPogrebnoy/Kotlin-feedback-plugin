package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.track.project

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.project.ProjectState
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.project.ProjectsStatisticState
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.ProjectsStatisticService
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user.hasVcs
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user.isKotlinProject
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import java.time.LocalDate

/**
 * Tracks open projects and stores information about them.
 */

class TrackOpenedProjectInfo : StartupActivity {

    private val projectsStatisticsService: ProjectsStatisticService = service()

    override fun runActivity(project: Project) {
        val state: ProjectsStatisticState = projectsStatisticsService.state ?: return
        state.projectsStatisticState[project.name] = ProjectState(
                LocalDate.now(),
                hasVcs(project),
                isKotlinProject(project)
        )
    }

}