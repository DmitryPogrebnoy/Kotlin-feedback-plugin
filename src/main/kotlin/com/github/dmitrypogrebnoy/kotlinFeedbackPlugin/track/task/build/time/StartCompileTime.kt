package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.track.task.build.time

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task.MAKE_TASK_NAME
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task.ProjectTask
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task.TempTaskInfo
import com.intellij.openapi.compiler.CompileContext
import com.intellij.openapi.compiler.CompileTask
import java.time.LocalTime

class StartCompileTime : CompileTask {
    override fun execute(context: CompileContext?): Boolean {
        if (context != null) {
            val projectTask = ProjectTask(context.project.name, MAKE_TASK_NAME)
            TempTaskInfo.taskStartTime[projectTask] = LocalTime.now().toSecondOfDay()
        }

        return true
    }
}