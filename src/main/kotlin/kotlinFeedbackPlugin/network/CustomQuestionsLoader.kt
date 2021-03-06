package kotlinFeedbackPlugin.network

import com.google.gson.JsonObject
import kotlinFeedbackPlugin.network.HttpClient.getJsonFileFromGithub
import kotlinFeedbackPlugin.user.CustomQuestion
import kotlinFeedbackPlugin.user.QuestionTextFieldSettings

/**
 * Downloads custom questions and related options for each user type from json file on Github.
 *
 * File name: "CustomQuestions.json".
 *
 * @see kotlinFeedbackPlugin.user.UserType
 */

object CustomQuestionsLoader {

    //TODO: Set right Github url
    private const val jsonQuestionsUrl: String = "https://api.github.com/repos/DmitryPogrebnoy/Kotlin-feedback-plugin/contents/src/main/resources/CustomQuestions.json?ref=master"

    private val questionsJsonString: JsonObject? = getJsonFileFromGithub(jsonQuestionsUrl)

    fun getBeginnerCustomQuestion(): CustomQuestion? {
        return getCustomQuestion("beginner_user_type")
    }

    fun getSimpleCustomQuestion(): CustomQuestion? {
        return getCustomQuestion("simple_user_type")
    }

    fun getActiveCustomQuestion(): CustomQuestion? {
        return getCustomQuestion("active_user_type")
    }

    fun getLostCustomQuestion(): CustomQuestion? {
        return getCustomQuestion("lost_user_type")
    }

    /**
     * Parses question form json
     *
     * @return CustomQuestion if the loading and parsing was successful.
     * And null if an error occurred while loading, if there is no question, or if the question is an empty string.
     */
    private fun getCustomQuestion(userType: String): CustomQuestion? {
        return if (questionsJsonString != null) {
            try {
                val beginnerUserInfo: JsonObject = questionsJsonString[userType].asJsonObject
                val questionString: String = beginnerUserInfo["question"].asString
                val textFieldSettings: JsonObject = beginnerUserInfo["text_field_settings"].asJsonObject
                val textFieldPlaceholder: String = textFieldSettings["placeholder"].asString
                if (questionString.isNotEmpty()) {
                    CustomQuestion(questionString, QuestionTextFieldSettings(textFieldPlaceholder))
                } else {
                    null
                }
            } catch (e: RuntimeException) {
                println(e.message)
                null
            }
        } else null
    }
}