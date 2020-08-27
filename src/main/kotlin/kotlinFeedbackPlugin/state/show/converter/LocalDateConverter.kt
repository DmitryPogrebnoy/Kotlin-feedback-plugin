package kotlinFeedbackPlugin.state.show.converter

import com.google.gson.GsonBuilder
import com.intellij.util.xmlb.Converter
import java.time.LocalDate

class LocalDateConverter : Converter<LocalDate>() {

    private val gson = GsonBuilder().create()

    override fun toString(value: LocalDate): String? {
        return gson.toJson(value)
    }

    override fun fromString(value: String): LocalDate? {
        return gson.fromJson(value, LocalDate::class.java)
    }
}