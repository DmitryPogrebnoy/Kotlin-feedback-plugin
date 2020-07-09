package com.github.dmitrypogrebnoy.feedbacktest.action

import com.github.dmitrypogrebnoy.feedbacktest.services.StatisticPersistentStateService
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service

class StatisticPluginShow : AnAction() {

    // more flexible way to store persistent state
    private val statisticService: StatisticPersistentStateService = service<StatisticPersistentStateService>()

    override fun actionPerformed(e: AnActionEvent) {
        val notification = Notification(
                "Statistic plugin",
                "Collected statistic",
                "Number of changes to Kotlin files - " + statisticService.state!!.editKotlinCount +
                        ".\nThe extension of the last modified file - " + statisticService.state!!.lastEditExtension,
                NotificationType.INFORMATION
        )

        notification.notify(e.project)
    }
}