package kotlinFeedbackPlugin.track.task.build.time

import com.intellij.openapi.compiler.CompileContext
import com.intellij.openapi.compiler.CompileTask
import kotlinFeedbackPlugin.state.task.IDEAProjectTask
import kotlinFeedbackPlugin.state.task.MAKE_TASK_NAME
import kotlinFeedbackPlugin.state.task.TempTaskInfo
import java.time.LocalTime

/**
 * Tracks the start of the compilation task and stores it in temporary storage.
 *
 * @see kotlinFeedbackPlugin.state.task.TempTaskInfo
 */

class StartCompileTime : CompileTask {
    override fun execute(context: CompileContext?): Boolean {
        if (context != null) {
            val projectTask = IDEAProjectTask(context.project.name, MAKE_TASK_NAME)
            TempTaskInfo.ideaTasksStartTime[projectTask] = LocalTime.now().toSecondOfDay()
        }

        return true
    }
}