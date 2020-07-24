package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.editor.converter

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.editor.EditInfo
import com.intellij.util.xmlb.Converter
import java.time.LocalDate

class EditorStatisticConverter : Converter<MutableMap<LocalDate, EditInfo>>() {
    /*
    Format: yyyy1-mm1-dd1=editInfo1;yyyy2-mm2-dd2=editInfo2;
    */

    override fun fromString(value: String): MutableMap<LocalDate, EditInfo>? {
        val listDateEditStatistic = value.split(';').dropLast(1)
        val editorStatistic: MutableMap<LocalDate, EditInfo> = mutableMapOf()
        for (item in listDateEditStatistic) {
            val splitItem = item.split("=")
            val localDate = LocalDate.parse(splitItem[0])
            editorStatistic[localDate] = EditInfo(splitItem[1].toLong())
        }
        return editorStatistic
    }

    override fun toString(value: MutableMap<LocalDate, EditInfo>): String? {
        val stringBuilder = StringBuilder()
        for (entry in value.entries) {
            stringBuilder.append(
                    "${entry.key}=${entry.value.numberEditing};"
            )
        }
        return stringBuilder.toString()
    }
}