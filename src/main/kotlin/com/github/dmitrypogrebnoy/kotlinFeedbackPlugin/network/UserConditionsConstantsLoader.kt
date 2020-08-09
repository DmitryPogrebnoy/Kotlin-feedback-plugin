package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.network

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.network.HttpClient.getJsonFileFromGithub
import com.google.gson.JsonObject

object UserConditionsConstantsLoader {

    //TODO: Set right Github url
    private const val userConstantsFileUrl: String = "https://api.github.com/repos/DmitryPogrebnoy/Kotlin-feedback-plugin/contents/src/main/resources/UserConditionsConstants.json?ref=master"

    private val jsonUserConstants: JsonObject? = getJsonFileFromGithub(userConstantsFileUrl)

    fun getMinNumberRelevantEditingKotlinFile(): Int? {
        return getConstant("min_number_relevant_editing_kotlin_file")
    }

    fun getNumberRelevantDays(): Int? {
        return getConstant("number_relevant_days")
    }

    fun getNumberDaysForRecentProjects(): Int? {
        return getConstant("number_days_for_recent_projects")
    }

    fun getNumberRecentKotlinProjectsWithoutVcs(): Int? {
        return getConstant("number_recent_kotlin_projects_without_vcs")
    }

    private fun getConstant(id: String): Int? {
        return if (jsonUserConstants != null) {
            try {
                jsonUserConstants[id].asInt
            } catch (e: RuntimeException) {
                println(e.message)
                null
            }
        } else null
    }
}