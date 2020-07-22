package com.github.dmitrypogrebnoy.feedbacktest.state.show.notificationFeedback

import com.github.dmitrypogrebnoy.feedbacktest.state.show.notificationFeedback.converter.LocalDateConverter
import com.intellij.util.xmlb.annotations.OptionTag
import java.time.LocalDate

data class DateFeedbackState(
        @OptionTag(converter = LocalDateConverter::class)
        var sendFeedbackDate: LocalDate = LocalDate.MIN,
        @OptionTag(converter = LocalDateConverter::class)
        var showFeedbackNotificationDate: LocalDate = LocalDate.MIN
)