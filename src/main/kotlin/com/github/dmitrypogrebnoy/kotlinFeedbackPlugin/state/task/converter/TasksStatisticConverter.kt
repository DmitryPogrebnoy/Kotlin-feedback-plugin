package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task.converter

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task.ProjectTask
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.task.TaskStatisticInfo
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.intellij.util.xmlb.Converter
import java.lang.reflect.Type


class TasksStatisticConverter : Converter<MutableMap<ProjectTask, TaskStatisticInfo>>() {

    private val gson = GsonBuilder().disableHtmlEscaping().enableComplexMapKeySerialization().create()

    override fun fromString(value: String): MutableMap<ProjectTask, TaskStatisticInfo>? {
        val type: Type = object : TypeToken<MutableMap<ProjectTask, TaskStatisticInfo>>() {}.type
        return gson.fromJson(value, type)
    }

    override fun toString(value: MutableMap<ProjectTask, TaskStatisticInfo>): String? {
        return gson.toJson(value)
    }
}