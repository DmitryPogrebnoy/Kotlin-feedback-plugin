package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.track.task.build.time

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.show.canShowNotificationInGradleExecuteTaskTime
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task.GRADLE_TASKS_FOR_TRACK
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task.GradleProjectTask
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task.GradleTaskTempInfo
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task.TempTaskInfo
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification.FeedbackNotification
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskId
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskNotificationListener
import com.intellij.openapi.project.Project
import org.jetbrains.plugins.gradle.service.task.GradleTaskManagerExtension
import org.jetbrains.plugins.gradle.settings.GradleExecutionSettings
import java.time.LocalTime

// Tracking starting time EXECUTE_TYPE type gradle tasks
class GradleTaskManager : GradleTaskManagerExtension {

    //Only executed if gradle task has EXECUTE_TYPE type
    override fun executeTasks(id: ExternalSystemTaskId,
                              taskNames: MutableList<String>,
                              projectPath: String,
                              settings: GradleExecutionSettings?,
                              jvmParametersSetup: String?,
                              listener: ExternalSystemTaskNotificationListener): Boolean {

        if (taskNames.size == 1 && GRADLE_TASKS_FOR_TRACK.contains(taskNames[0])) {
            val project = id.findProject() ?: return false

            showFeedbackNotification(project, taskNames[0])

            TempTaskInfo.gradleExecuteTasksStartInfo[
                    GradleProjectTask(
                            project.name,
                            id.toString()
                    )
            ] = GradleTaskTempInfo(LocalTime.now().toSecondOfDay(), taskNames[0])
        }

        //should return false, else not show output and strange behavior
        return false
    }

    override fun cancelTask(id: ExternalSystemTaskId, listener: ExternalSystemTaskNotificationListener): Boolean {
        //should return true, else error
        return true
    }

    private fun showFeedbackNotification(project: Project, taskName: String) {
        if (canShowNotificationInGradleExecuteTaskTime(project, taskName)) {
            FeedbackNotification(project).trackingNotify()
        }
    }
}