package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.editor.converter

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.editor.EditInfo
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.intellij.util.xmlb.Converter
import java.lang.reflect.Type
import java.time.LocalDate

class EditorStatisticConverter : Converter<MutableMap<LocalDate, EditInfo>>() {

    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()

    override fun fromString(value: String): MutableMap<LocalDate, EditInfo>? {
        val type: Type = object : TypeToken<MutableMap<LocalDate, EditInfo>?>() {}.type
        return gson.fromJson(value, type)
    }

    override fun toString(value: MutableMap<LocalDate, EditInfo>): String? {
        return gson.toJson(value)
    }
}