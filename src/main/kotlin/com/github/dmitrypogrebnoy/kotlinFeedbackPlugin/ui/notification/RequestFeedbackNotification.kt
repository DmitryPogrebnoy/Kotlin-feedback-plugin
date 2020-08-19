package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.bundle.FeedbackBundle
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.FeedbackDatesService
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.show.FeedbackDatesState
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader
import java.time.LocalDate

/**
 * Basic notification for Kotlin feedback requests
 */

class RequestFeedbackNotification(
        titleText: String = FeedbackBundle.message("request.feedback.default.notification.title"),
        descriptionText: String = FeedbackBundle.message("request.feedback.default.notification.content")
) : Notification(
        KotlinFeedbackNotificationGroup.group.displayId,
        //TODO: Set right icon
        IconLoader.getIcon("/kotlin.svg"),
        titleText, "",
        descriptionText,
        NotificationType.INFORMATION, null
) {

    //Tracking showing notification
    override fun notify(project: Project?) {
        val feedbackDatesState: FeedbackDatesState = service<FeedbackDatesService>().state ?: return
        super.notify(project)
        feedbackDatesState.dateShowFeedbackNotification = LocalDate.now()
    }
}