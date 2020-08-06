package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.bundle.FeedbackBundle
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.DateFeedbackStatService
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.show.DateFeedbackState
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import java.time.LocalDate

class RequestFeedbackNotification(
        titleText: String = FeedbackBundle.message("request.feedback.default.notification.title"),
        descriptionText: String = FeedbackBundle.message("request.feedback.default.notification.content")
) : Notification(
        KotlinFeedbackNotificationGroup.group.displayId,
        titleText,
        descriptionText,
        NotificationType.INFORMATION
) {

    //Tracking showing notification
    override fun notify(project: Project?) {
        val dateFeedbackState: DateFeedbackState = service<DateFeedbackStatService>().state ?: return
        super.notify(project)
        dateFeedbackState.dateShowFeedbackNotification = LocalDate.now()
    }
}