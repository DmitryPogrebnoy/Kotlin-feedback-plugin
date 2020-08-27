package kotlinFeedbackPlugin.network

import com.google.gson.JsonObject
import kotlinFeedbackPlugin.network.HttpClient.getJsonFileFromGithub

/**
 * Downloads plugin user conditions constants from json file on Github.
 *
 * File name: "UserConditionsConstants.json".
 *
 * @see kotlinFeedbackPlugin.user.UserConditionsKt
 */

object UserConditionsConstantsLoader {

    //TODO: Set right Github url
    private const val userConstantsFileUrl: String = "https://api.github.com/repos/DmitryPogrebnoy/Kotlin-feedback-plugin/contents/src/main/resources/UserConditionsConstants.json?ref=master"

    private val jsonUserConstants: JsonObject? = getJsonFileFromGithub(userConstantsFileUrl)

    fun getMinNumberRelevantEditingKotlinFiles(): Int? {
        return getConstant("min_number_relevant_editing_kotlin_files")
    }

    fun getNumberRelevantDaysEditingKotlinFiles(): Int? {
        return getConstant("number_relevant_days_editing_kotlin_files")
    }

    fun getNumberDaysForRecentProjects(): Int? {
        return getConstant("number_days_for_recent_projects")
    }

    fun getNumberRecentKotlinProjectsWithoutVcs(): Int? {
        return getConstant("number_recent_kotlin_projects_without_vcs")
    }

    fun getNumberDaysWithoutEditingKotlinFiles(): Int? {
        return getConstant("number_days_without_editing_kotlin_files")
    }

    fun getNumberDaysPreviousEditingKotlinFiles(): Int? {
        return getConstant("number_days_previous_editing_kotlin_files")
    }

    fun getMinNumberPreviousEditingKotlinFiles(): Int? {
        return getConstant("min_number_previous_editing_kotlin_files")
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