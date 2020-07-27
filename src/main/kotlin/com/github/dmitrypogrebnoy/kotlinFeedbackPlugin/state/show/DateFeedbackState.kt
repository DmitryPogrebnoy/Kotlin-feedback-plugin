package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.show

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.show.converter.LocalDateConverter
import com.intellij.util.xmlb.annotations.OptionTag
import java.time.LocalDate

data class DateFeedbackState(
        @OptionTag(converter = LocalDateConverter::class)
        var sendFeedbackDate: LocalDate = LocalDate.now(),
        @OptionTag(converter = LocalDateConverter::class)
        var showFeedbackNotificationDate: LocalDate = LocalDate.now()
)