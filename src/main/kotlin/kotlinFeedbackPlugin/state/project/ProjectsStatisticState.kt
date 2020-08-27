package kotlinFeedbackPlugin.state.project

import com.intellij.util.xmlb.annotations.OptionTag
import kotlinFeedbackPlugin.state.project.converter.ProjectsStatisticConverter

/**
 * Storage for projects statistic.
 *
 * @see kotlinFeedbackPlugin.state.project.ProjectState
 */

data class ProjectsStatisticState(
        @OptionTag(converter = ProjectsStatisticConverter::class)
        val projectsStatisticState: MutableMap<String, ProjectState> = mutableMapOf()
)