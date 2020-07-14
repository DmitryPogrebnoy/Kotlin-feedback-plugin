package com.github.dmitrypogrebnoy.feedbacktest.task.build

import com.github.dmitrypogrebnoy.feedbacktest.notification.FeedbackNotification
import com.github.dmitrypogrebnoy.feedbacktest.services.EditorStatisticService
import com.github.dmitrypogrebnoy.feedbacktest.services.TasksStatisticService
import com.github.dmitrypogrebnoy.feedbacktest.state.editor.EditorState
import com.github.dmitrypogrebnoy.feedbacktest.state.task.MAKE_TASK_NAME
import com.github.dmitrypogrebnoy.feedbacktest.state.task.ProjectTask
import com.github.dmitrypogrebnoy.feedbacktest.state.task.TasksStatisticState
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.application.ex.ApplicationInfoEx
import com.intellij.openapi.compiler.CompileContext
import com.intellij.openapi.compiler.CompileTask
import com.intellij.openapi.components.service
import com.intellij.openapi.extensions.PluginId

class CompileFeedbackNotification : CompileTask {
    override fun execute(context: CompileContext?): Boolean {
        if (context != null) {
            val applicationInfoEx: ApplicationInfoEx = ApplicationInfoEx.getInstanceEx()

            val kotlinPluginDescriptor: IdeaPluginDescriptor = PluginManagerCore.getPlugin(
                    PluginId.findId(KOTLIN_PLUGIN_ID)
            ) ?: return true

            val taskStatisticState: TasksStatisticState = service<TasksStatisticService>().state
                    ?: return false
            val editorState: EditorState = service<EditorStatisticService>().state ?: return false
            val curProjectTask = ProjectTask(context.project.name, MAKE_TASK_NAME)
            if (taskStatisticState.projectsTasksInfo.containsKey(curProjectTask)) {
                val statisticInfo = taskStatisticState.projectsTasksInfo[curProjectTask]!!
                //Set lastTaskDurationTime > 0 and numberEditKotlinFile > 0 for test
                if (statisticInfo.lastTaskDurationTime > 0 && editorState.numberEditKotlinFile > 0 &&
                        //Set '!' for test
                        (!applicationInfoEx.isEAP ||
                                //Set '!' for test
                                !(kotlinPluginDescriptor.version.contains("M")
                                        || kotlinPluginDescriptor.version.contains("eap"))
                                )
                ) {
                    FeedbackNotification(context.project).notificationNotify()
                }
            }
        }
        return true
    }
}