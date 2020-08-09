package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.network.CustomQuestionsLoader.getActiveCustomQuestion
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.setting.FunctionalitySettings.enableNotification
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.dialog.ActiveUserFeedbackDialog
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification.ActiveUserTypeNotificationAction
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification.RequestFeedbackNotification
import com.intellij.openapi.project.Project

object ActiveUserType : UserType {

    override val userTypeName: String = "Active Kotlin user"

    override var customQuestion: CustomQuestion? = getActiveCustomQuestion()

    override fun isUserSatisfiesUserType(): Boolean {
        return needCollectUserFeedback() && isKotlinPluginEAP() && isKotlinPluginEnabled()
                && checkRelevantNumberKotlinFileEditing()
    }

    override fun showFeedbackNotification(project: Project) {
        if (enableNotification) {
            val notification = RequestFeedbackNotification()
            notification.addAction(ActiveUserTypeNotificationAction())
            notification.notify(project)
        }
    }

    override fun showFeedbackDialog(project: Project) {
        ActiveUserFeedbackDialog(project).show()
    }
}