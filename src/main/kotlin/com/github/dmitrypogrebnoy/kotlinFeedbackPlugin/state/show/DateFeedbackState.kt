package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.show

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.show.converter.LocalDateConverter
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.widget.FeedbackWidget
import com.intellij.util.xmlb.annotations.OptionTag
import java.time.LocalDate

class DateFeedbackState(
        curDateSendFeedback: LocalDate = LocalDate.ofYearDay(1900, 1),
        curDateShowFeedbackNotification: LocalDate = LocalDate.ofYearDay(1900, 1),
        curDateCloseFeedbackDialog: LocalDate = LocalDate.ofYearDay(1900, 1)
) {

        @OptionTag(converter = LocalDateConverter::class)
        var dateSendFeedback: LocalDate = curDateSendFeedback
                set(value) {
                        field = value
                        FeedbackWidget.updateFeedbackWidgets()
                }

        @OptionTag(converter = LocalDateConverter::class)
        var dateShowFeedbackNotification: LocalDate = curDateShowFeedbackNotification
                set(value) {
                        field = value
                        FeedbackWidget.updateFeedbackWidgets()
                }

        @OptionTag(converter = LocalDateConverter::class)
        var dateCloseFeedbackDialog: LocalDate = curDateCloseFeedbackDialog
                set(value) {
                        field = value
                        FeedbackWidget.updateFeedbackWidgets()
                }


        override fun equals(other: Any?): Boolean {
                return if (other is DateFeedbackState) {
                        (dateSendFeedback == other.dateSendFeedback
                                && dateShowFeedbackNotification == other.dateShowFeedbackNotification
                                && dateCloseFeedbackDialog == other.dateCloseFeedbackDialog)
                } else false
        }

        override fun hashCode(): Int {
                var hash = 7
                hash = 31 * hash + dateSendFeedback.hashCode()
                hash = 31 * hash + dateShowFeedbackNotification.hashCode()
                hash = 31 * hash + dateCloseFeedbackDialog.hashCode()
                return hash
        }
}