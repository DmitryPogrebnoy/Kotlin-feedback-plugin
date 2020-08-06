package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.dialog.BeginnerUserFeedbackDialog
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification.BeginnerUserTypeNotificationAction
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification.RequestFeedbackNotification
import com.intellij.openapi.project.Project

object BeginnerUserType : UserType {

    override val userTypeName: String = "Beginner Kotlin user"

    override fun isUserSatisfiesUserType(): Boolean {
        return needCollectUserFeedback()
                && isKotlinPluginEnabled()
                && checkRelevantNumberKotlinFileEditing()
                && (isEduToolsPluginEnabled() || openedManyRecentProjectsWithoutVcs())
    }

    override fun showFeedbackNotification(project: Project) {
        val notification = RequestFeedbackNotification()
        notification.addAction(BeginnerUserTypeNotificationAction())
        notification.notify(project)
    }

    override fun showFeedbackDialog(project: Project) {
        BeginnerUserFeedbackDialog(project).show()
    }
}