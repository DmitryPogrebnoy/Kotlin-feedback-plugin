package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.network

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user.CustomQuestion
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user.QuestionTextFieldSettings
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.apache.commons.httpclient.Header
import org.apache.commons.httpclient.HttpStatus
import org.apache.commons.httpclient.methods.GetMethod
import java.io.IOException
import java.util.*

object CustomQuestionsLoader {

    //TODO: Set right Github url
    private const val jsonQuestionsUrl: String = "https://api.github.com/repos/DmitryPogrebnoy/Kotlin-feedback-plugin/contents/src/main/resources/CustomQuestions.json?ref=master"

    private val questionsJsonString: JsonObject? = getJsonQuestions()

    fun getBeginnerCustomQuestion(): CustomQuestion? {
        return getCustomQuestion("beginner_user_type")
    }

    fun getSimpleCustomQuestion(): CustomQuestion? {
        return getCustomQuestion("simple_user_type")
    }

    fun getActiveCustomQuestion(): CustomQuestion? {
        return getCustomQuestion("active_user_type")
    }

    private fun getCustomQuestion(userType: String): CustomQuestion? {
        return if (questionsJsonString != null) {
            try {
                val beginnerUserInfo: JsonObject = questionsJsonString[userType].asJsonObject
                val questionString: String = beginnerUserInfo["question"].asString
                val textFieldSettings: JsonObject = beginnerUserInfo["text_field_settings"].asJsonObject
                val textFieldWidth: Int = textFieldSettings["height"].asInt
                val textFieldPlaceholder: String = textFieldSettings["placeholder"].asString
                if (questionString.isNotEmpty()) {
                    CustomQuestion(questionString, QuestionTextFieldSettings(textFieldWidth, textFieldPlaceholder))
                } else {
                    null
                }
            } catch (e: RuntimeException) {
                println(e.message)
                null
            }
        } else null
    }

    private fun getJsonQuestions(): JsonObject? {
        val getQuestionsMethod = GetMethod(jsonQuestionsUrl)
        getQuestionsMethod.addRequestHeader(Header("Accept:", "application/vnd.github.v3+json"))
        getQuestionsMethod.addRequestHeader(Header("Authorisation:", "token ${System.getenv("GithubPermanentToken")}"))

        try {
            val statusCode = HttpClient.executeMethod(getQuestionsMethod)
            val responseBody = getQuestionsMethod.responseBodyAsStream.reader().readText()
            if (statusCode != HttpStatus.SC_OK) {
                getQuestionsMethod.releaseConnection()
                return null
            }
            val responseBodyJson = JsonParser.parseString(responseBody).asJsonObject
            getQuestionsMethod.releaseConnection()
            val contentQuestionsFile = responseBodyJson["content"].asString
            val decodedQuestionsFile = Base64.getMimeDecoder().decode(contentQuestionsFile).toString(Charsets.UTF_8)
            return JsonParser.parseString(decodedQuestionsFile).asJsonObject
        } catch (e: IOException) {
            println(e.message)
        } catch (e: IllegalArgumentException) {
            println(e.message)
        } finally {
            getQuestionsMethod.releaseConnection()
        }
        return null
    }
}