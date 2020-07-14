package com.github.dmitrypogrebnoy.feedbacktest.task.build.time

import com.github.dmitrypogrebnoy.feedbacktest.services.TasksStatisticService
import com.github.dmitrypogrebnoy.feedbacktest.state.task.*
import com.intellij.openapi.compiler.CompileContext
import com.intellij.openapi.compiler.CompileTask
import com.intellij.openapi.components.service
import java.time.LocalTime

class StartCompileTime : CompileTask {
    override fun execute(context: CompileContext?): Boolean {
        if (context != null) {
            val tasksStatisticState: TasksStatisticState = service<TasksStatisticService>().state!!
            val projectTask = ProjectTask(context.project.name, MAKE_TASK_NAME)
            tasksStatisticState.projectsTasksInfo[projectTask] = StatisticInfo(
                    LocalTime.now().toSecondOfDay() -
                            tasksStatisticState.projectsTasksInfo[projectTask]!!.startTaskTime,
                    TASK_DEFAULT_START_TIME
            )
        }

        return true
    }
}