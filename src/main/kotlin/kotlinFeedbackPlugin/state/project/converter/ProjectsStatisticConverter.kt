package kotlinFeedbackPlugin.state.project.converter

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.intellij.util.xmlb.Converter
import kotlinFeedbackPlugin.state.project.ProjectState
import java.lang.reflect.Type

class ProjectsStatisticConverter : Converter<MutableMap<String, ProjectState>>() {

    private val gson = GsonBuilder().create()

    override fun fromString(value: String): MutableMap<String, ProjectState>? {
        val type: Type = object : TypeToken<MutableMap<String, ProjectState>?>() {}.type
        return gson.fromJson(value, type)
    }

    override fun toString(value: MutableMap<String, ProjectState>): String? {
        return gson.toJson(value)
    }
}