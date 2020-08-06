package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.track.task.build.time

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.show.showGradleRefreshTaskTimeNotificationIfPossible
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.show.showGradleResolveTaskTimeNotificationIfPossible
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.TasksStatisticService
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task.*
import com.intellij.openapi.components.service
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskId
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskNotificationEvent
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskNotificationListener
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskType
import com.intellij.openapi.project.Project
import java.time.LocalTime

// Tracking end time gradle EXECUTE_TYPE type task.
// Full tracking time gradle RESOLVE_PROJECT and REFRESH_TASKS_LIST types task.
class GradleTaskListener : ExternalSystemTaskNotificationListener {

    private val tasksStatisticService: TasksStatisticService = service()

    override fun onSuccess(id: ExternalSystemTaskId) {
        if (id.projectSystemId.id == GRADLE_PROJECT_SYSTEM_ID) {

            val project: Project = id.findProject() ?: return
            val tasksStatState = tasksStatisticService.state ?: return

            when (id.type) {
                ExternalSystemTaskType.EXECUTE_TASK -> {
                    val taskTempInfo = TempTaskInfo.gradleExecuteTasksStartInfo
                            .remove(GradleProjectTask(project.name, id.toString())) ?: return
                    tasksStatState.projectsTasksInfo[
                            ProjectTask(
                                    project.name,
                                    taskTempInfo.taskName
                            )
                    ] = TaskStatisticInfo(
                            LocalTime.now().toSecondOfDay() - taskTempInfo.startTime
                    )
                }
                ExternalSystemTaskType.RESOLVE_PROJECT -> {
                    val resolveProjectTaskTime = TempTaskInfo.gradleResolveProjectTasksStartTime
                            .remove(project.name) ?: return
                    tasksStatState.projectsTasksInfo[ProjectTask(project.name, GRADLE_RESOLVE_PROJECT)] =
                            TaskStatisticInfo(
                                    LocalTime.now().toSecondOfDay() - resolveProjectTaskTime
                            )
                }
                ExternalSystemTaskType.REFRESH_TASKS_LIST -> {
                    val refreshTasksListStartTime = TempTaskInfo
                            .gradleRefreshTaskListsTasksStartTime.remove(project.name) ?: return
                    tasksStatState.projectsTasksInfo[ProjectTask(project.name, GRADLE_REFRESH_TASKS_LISTS_PROJECT)] =
                            TaskStatisticInfo(
                                    LocalTime.now().toSecondOfDay() - refreshTasksListStartTime
                            )
                }
            }
        }
    }

    override fun onFailure(id: ExternalSystemTaskId, e: Exception) {
        clearTemp(id)
    }

    override fun onTaskOutput(id: ExternalSystemTaskId, text: String, stdOut: Boolean) {}

    override fun onStatusChange(event: ExternalSystemTaskNotificationEvent) {}

    override fun onCancel(id: ExternalSystemTaskId) {
        clearTemp(id)
    }

    override fun onEnd(id: ExternalSystemTaskId) {}

    override fun beforeCancel(id: ExternalSystemTaskId) {}

    override fun onStart(id: ExternalSystemTaskId) {}

    override fun onStart(id: ExternalSystemTaskId, workingDir: String?) {
        if (id.projectSystemId.id == GRADLE_PROJECT_SYSTEM_ID) {
            when (id.type) {
                ExternalSystemTaskType.EXECUTE_TASK -> {
                }
                ExternalSystemTaskType.RESOLVE_PROJECT -> {
                    val project = id.findProject() ?: return

                    //show feedback notification if all condition true
                    showGradleRefreshTaskTimeNotificationIfPossible(project)

                    TempTaskInfo.gradleResolveProjectTasksStartTime[project.name] = LocalTime.now().toSecondOfDay()
                }
                ExternalSystemTaskType.REFRESH_TASKS_LIST -> {
                    val project = id.findProject() ?: return

                    //show feedback notification if all condition true
                    showGradleResolveTaskTimeNotificationIfPossible(project)

                    TempTaskInfo.gradleRefreshTaskListsTasksStartTime[project.name] = LocalTime.now().toSecondOfDay()
                }
            }
        }
    }

    //Clear temp on cancel or failure task
    private fun clearTemp(id: ExternalSystemTaskId) {
        if (id.projectSystemId.id == GRADLE_PROJECT_SYSTEM_ID) {
            when (id.type) {
                ExternalSystemTaskType.EXECUTE_TASK -> {
                    val project: Project = id.findProject() ?: return
                    TempTaskInfo.gradleExecuteTasksStartInfo.remove(GradleProjectTask(project.name, id.toString()))
                }
                ExternalSystemTaskType.RESOLVE_PROJECT -> {
                    val project: Project = id.findProject() ?: return
                    TempTaskInfo.gradleResolveProjectTasksStartTime.remove(project.name)
                }
                ExternalSystemTaskType.REFRESH_TASKS_LIST -> {
                    val project: Project = id.findProject() ?: return
                    TempTaskInfo.gradleRefreshTaskListsTasksStartTime.remove(project.name)
                }
            }
        }
    }
}