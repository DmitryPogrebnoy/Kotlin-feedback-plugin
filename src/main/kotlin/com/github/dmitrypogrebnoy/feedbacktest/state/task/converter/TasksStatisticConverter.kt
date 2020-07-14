package com.github.dmitrypogrebnoy.feedbacktest.state.task.converter

import com.github.dmitrypogrebnoy.feedbacktest.state.task.ProjectTask
import com.github.dmitrypogrebnoy.feedbacktest.state.task.StatisticInfo
import com.intellij.util.xmlb.Converter


class TasksStatisticConverter : Converter<MutableMap<ProjectTask, StatisticInfo>>() {

    override fun fromString(value: String): MutableMap<ProjectTask, StatisticInfo>? {
        val listProjectsTasksInfo = value.split(';').dropLast(1)
        val projectsTasksInfoMap: MutableMap<ProjectTask, StatisticInfo> = mutableMapOf()
        for (projectTaskInfo in listProjectsTasksInfo) {
            val splitProjectTaskInfo = projectTaskInfo.split('=')
            val splitProjectTask = splitProjectTaskInfo[0].split(',')
            val splitStatisticInfo = splitProjectTaskInfo[1].split(',')
            projectsTasksInfoMap[ProjectTask(splitProjectTask[0], splitProjectTask[1])] =
                    StatisticInfo(splitStatisticInfo[0].toInt(), splitStatisticInfo[1].toInt())
        }
        return projectsTasksInfoMap
    }

    override fun toString(value: MutableMap<ProjectTask, StatisticInfo>): String? {
        val stringBuilder = StringBuilder()
        for (entry in value.entries) {
            stringBuilder.append(
                    "${entry.key.projectName},${entry.key.taskName}=" +
                            "${entry.value.lastTaskDurationTime},${entry.value.startTaskTime};"
            )
        }
        return stringBuilder.toString()
    }
}