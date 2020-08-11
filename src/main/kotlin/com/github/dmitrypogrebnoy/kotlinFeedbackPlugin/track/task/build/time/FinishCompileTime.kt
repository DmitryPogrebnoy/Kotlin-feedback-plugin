package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.track.task.build.time

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.TasksStatisticService
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task.*
import com.intellij.openapi.compiler.CompileContext
import com.intellij.openapi.compiler.CompileTask
import com.intellij.openapi.components.service
import java.time.LocalTime

/**
 * Tracks the end of the compilation task and saves its duration.
 *
 * @see com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task.TasksStatisticState
 */

class FinishCompileTime : CompileTask {
    override fun execute(context: CompileContext?): Boolean {
        if (context != null) {
            val tasksStatisticState: TasksStatisticState = service<TasksStatisticService>().state!!
            val projectTask = IDEAProjectTask(context.project.name, MAKE_TASK_NAME)

            if (TempTaskInfo.ideaTasksStartTime.contains(projectTask)) {
                tasksStatisticState.projectsTasksInfo[
                        ProjectTask(
                                projectTask.projectName,
                                projectTask.taskName
                        )
                ] = TaskStatisticInfo(
                        LocalTime.now().toSecondOfDay()
                                - TempTaskInfo.ideaTasksStartTime[projectTask]!!
                )
                TempTaskInfo.ideaTasksStartTime.remove(projectTask)
            }
        }

        return true
    }
}