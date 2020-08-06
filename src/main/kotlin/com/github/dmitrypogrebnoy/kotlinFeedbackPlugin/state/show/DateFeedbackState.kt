package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.show

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.show.converter.LocalDateConverter
import com.intellij.util.xmlb.annotations.OptionTag
import java.time.LocalDate

data class DateFeedbackState(
        @OptionTag(converter = LocalDateConverter::class)
        var dateSendFeedback: LocalDate = LocalDate.MIN,
        @OptionTag(converter = LocalDateConverter::class)
        var dateShowFeedbackNotification: LocalDate = LocalDate.MIN,
        @OptionTag(converter = LocalDateConverter::class)
        var dateCloseFeedbackDialog: LocalDate = LocalDate.MIN
)