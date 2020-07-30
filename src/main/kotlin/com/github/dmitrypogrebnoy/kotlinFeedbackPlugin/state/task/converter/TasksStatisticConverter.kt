package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task.converter

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task.ProjectTask
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task.TaskStatisticInfo
import com.intellij.util.xmlb.Converter


class TasksStatisticConverter : Converter<MutableMap<ProjectTask, TaskStatisticInfo>>() {
    /*
    Format: projectName1,taskName1=lastTime1;projectName2,taskName2=lastTime2;
     */
    override fun fromString(value: String): MutableMap<ProjectTask, TaskStatisticInfo>? {
        val listProjectsTasksInfo = value.split(';').dropLast(1)
        val projectsTasksInfoMap: MutableMap<ProjectTask, TaskStatisticInfo> = mutableMapOf()
        for (item in listProjectsTasksInfo) {
            val splitProjectTaskInfo = item.split('=')
            val splitProjectTask = splitProjectTaskInfo[0].split(',')
            projectsTasksInfoMap[ProjectTask(splitProjectTask[0], splitProjectTask[1])] =
                    TaskStatisticInfo(splitProjectTaskInfo[1].toInt())
        }
        return projectsTasksInfoMap
    }

    override fun toString(value: MutableMap<ProjectTask, TaskStatisticInfo>): String? {
        val stringBuilder = StringBuilder()
        for (entry in value.entries) {
            stringBuilder.append(
                    "${entry.key.projectName},${entry.key.taskName}=" +
                            "${entry.value.lastTaskDurationTime};"
            )
        }
        return stringBuilder.toString()
    }
}