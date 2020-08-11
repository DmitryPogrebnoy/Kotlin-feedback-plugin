package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.network

import com.google.gson.JsonObject

/**
 * Downloads plugin functionality setting from json file on Github.
 *
 * File name on Github: "FunctionalitySettings.json".
 *
 * @see com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.setting.FunctionalitySettings
 */

object FunctionalitySettingsLoader {

    //TODO: Set right Github url
    private const val settingsFileUrl: String = "https://api.github.com/repos/DmitryPogrebnoy/Kotlin-feedback-plugin/contents/src/main/resources/FunctionalitySettings.json?ref=master"

    private val jsonSettings: JsonObject? = HttpClient.getJsonFileFromGithub(settingsFileUrl)

    fun getEnabledNotification(): Boolean? {
        return getEnabledStatus("enable_notification")
    }

    fun getEnableWidgetIconColor(): Boolean? {
        return getEnabledStatus("enable_widget_icon_color")
    }

    fun getEnabledWidget(): Boolean? {
        return getEnabledStatus("enable_widget")
    }

    private fun getEnabledStatus(id: String): Boolean? {
        return if (jsonSettings != null) {
            try {
                jsonSettings[id].asBoolean
            } catch (e: RuntimeException) {
                println(e.message)
                null
            }
        } else null
    }
}