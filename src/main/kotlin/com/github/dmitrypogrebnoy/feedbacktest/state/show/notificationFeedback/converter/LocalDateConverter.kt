package com.github.dmitrypogrebnoy.feedbacktest.state.show.notificationFeedback.converter

import com.intellij.util.xmlb.Converter
import java.time.LocalDate

class LocalDateConverter : Converter<LocalDate>() {
    /*
    Format: yyyy-mm-dd
    */
    override fun toString(value: LocalDate): String? {
        return value.toString()
    }

    override fun fromString(value: String): LocalDate? {
        return LocalDate.parse(value)
    }
}