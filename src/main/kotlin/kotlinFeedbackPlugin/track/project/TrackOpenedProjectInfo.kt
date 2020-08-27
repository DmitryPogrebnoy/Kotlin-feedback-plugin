package kotlinFeedbackPlugin.track.project

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import kotlinFeedbackPlugin.state.project.ProjectState
import kotlinFeedbackPlugin.state.project.ProjectsStatisticState
import kotlinFeedbackPlugin.state.services.ProjectsStatisticService
import kotlinFeedbackPlugin.user.hasVcs
import kotlinFeedbackPlugin.user.isKotlinProject
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