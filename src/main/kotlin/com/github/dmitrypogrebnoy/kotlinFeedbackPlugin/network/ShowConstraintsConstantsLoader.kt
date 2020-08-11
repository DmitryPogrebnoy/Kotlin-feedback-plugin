package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.network

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.network.HttpClient.getJsonFileFromGithub
import com.google.gson.JsonObject

/**
 * Downloads show constraints constants from json file on Github.
 *
 * File name: "ShowConstraintsConstants.json".
 *
 * @see com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.show.ShowConstraintsKt
 */

object ShowConstraintsConstantsLoader {

    //TODO: Set right Github url
    private const val showConstraintsUrl: String = "https://api.github.com/repos/DmitryPogrebnoy/Kotlin-feedback-plugin/contents/src/main/resources/ShowConstraintsConstants.json?ref=master"

    private val jsonShowConstants: JsonObject? = getJsonFileFromGithub(showConstraintsUrl)

    fun getMinDaysSinceSendFeedback(): Int? {
        return getConstant("min_days_since_send_feedback")
    }

    fun getMinDaysSinceCloseFeedbackDialog(): Int? {
        return getConstant("min_days_since_close_feedback_dialog")
    }

    fun getMinDaysSinceShowNotification(): Int? {
        return getConstant("min_days_since_show_notification")
    }

    fun getMinDurationCompileTask(): Int? {
        return getConstant("min_duration_compile_task")
    }

    fun getMinDurationGradleTask(): Int? {
        return getConstant("min_duration_gradle_task")
    }

    fun getMinInactiveTime(): Int? {
        return getConstant("min_inactive_time")
    }

    private fun getConstant(id: String): Int? {
        return if (jsonShowConstants != null) {
            try {
                jsonShowConstants[id].asInt
            } catch (e: RuntimeException) {
                println(e.message)
                null
            }
        } else null
    }
}