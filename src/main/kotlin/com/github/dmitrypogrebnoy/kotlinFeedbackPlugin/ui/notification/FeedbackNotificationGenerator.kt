package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.bundle.FeedbackBundle

/**
 * Functions for generating random feedback notifications.
 *
 */

fun generateRequestFeedbackNotification(): RequestFeedbackNotification {
    return when ((0..2).random()) {
        1 -> RequestFeedbackNotification(
                FeedbackBundle.message("request.feedback.first.notification.title"),
                FeedbackBundle.message("request.feedback.first.notification.content")
        )
        2 -> RequestFeedbackNotification(
                FeedbackBundle.message("request.feedback.second.notification.title"),
                FeedbackBundle.message("request.feedback.second.notification.content")
        )
        else -> RequestFeedbackNotification()
    }
}

fun generateSuccessSendFeedbackNotification(): SuccessSendFeedbackNotification {
    return when ((0..2).random()) {
        1 -> SuccessSendFeedbackNotification(
                FeedbackBundle.message("success.send.feedback.first.notification.title"),
                FeedbackBundle.message("success.send.feedback.first.notification.content")
        )
        2 -> SuccessSendFeedbackNotification(
                FeedbackBundle.message("success.send.feedback.second.notification.title"),
                FeedbackBundle.message("success.send.feedback.second.notification.content")
        )
        else -> SuccessSendFeedbackNotification()
    }
}
