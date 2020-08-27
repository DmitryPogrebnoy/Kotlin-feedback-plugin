package kotlinFeedbackPlugin.state.task

import com.intellij.util.xmlb.annotations.OptionTag
import kotlinFeedbackPlugin.state.task.converter.TasksStatisticConverter

/**
 * Storage for tasks statistic.
 *
 * @see kotlinFeedbackPlugin.state.task.ProjectTask
 * @see kotlinFeedbackPlugin.state.task.TaskStatisticInfo
 */

data class TasksStatisticState(
        @OptionTag(converter = TasksStatisticConverter::class)
        val projectsTasksInfo: MutableMap<ProjectTask, TaskStatisticInfo> = mutableMapOf())