package com.github.dmitrypogrebnoy.feedbacktest.action

import com.github.dmitrypogrebnoy.feedbacktest.dialog.FeedbackDialog
import com.github.dmitrypogrebnoy.feedbacktest.services.StatisticPersistentStateService
import com.intellij.ide.util.PropertiesComponent
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

class FeedbackNotificationAction : AnAction() {
    //simple way to store persistent state
    private val propComp: PropertiesComponent = PropertiesComponent.getInstance()

    // more flexible way to store persistent state
    private val statisticService: StatisticPersistentStateService = service<StatisticPersistentStateService>()

    init {
        if (!propComp.isValueSet("CountRunNotification")) {
            propComp.setValue("CountRunNotification", 0.toString(), (-1).toString())
        }

    }

    override fun actionPerformed(e: AnActionEvent) {
        propComp.setValue(
                "CountRunNotification",
                (propComp.getInt("CountRunNotification", 0)) + 1, -1
        )
        statisticService.state!!.count = statisticService.state!!.count + 1
        val notification: Notification = Notification(
                "Feedback notification",
                "Help make it better",
                " Tell us about your experience. " +
                        "Saved number ${propComp.getInt("CountRunNotification", 0)}" +
                        " " + statisticService.state!!.count,
                NotificationType.INFORMATION
        ).addAction(
                NotificationAction.createSimple("Give feedback") {
                    val dialog = FeedbackDialog(e.project!!)
                    dialog.show()
                }
        )

        notification.notify(e.project)
    }

    override fun update(e: AnActionEvent) {
        val project: Project? = e.project
        e.presentation.isEnabledAndVisible = project != null
    }
}
