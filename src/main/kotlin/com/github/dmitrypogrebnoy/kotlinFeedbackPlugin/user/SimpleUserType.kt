package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.network.CustomQuestionsLoader.getSimpleCustomQuestion
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.setting.FunctionalitySettings.enableNotification
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.dialog.SimpleUserFeedbackDialog
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification.RequestFeedbackNotification
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification.SimpleUserTypeNotificationAction
import com.intellij.openapi.project.Project

/**
 * This object represents the simple user type.
 *
 * @see com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user.UserType
 * @see com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user.UserTypeResolver
 */

object SimpleUserType : UserType {

    override val userTypeName: String = "Simple Kotlin user"

    override var customQuestion: CustomQuestion? = getSimpleCustomQuestion()

    override fun isUserSatisfiesUserType(): Boolean {
        return isSendFusEnabled() && checkRelevantNumberKotlinFileEditing()
    }

    override fun showFeedbackNotification(project: Project) {
        if (enableNotification) {
            val notification = RequestFeedbackNotification()
            notification.addAction(SimpleUserTypeNotificationAction())
            notification.notify(project)
        }
    }

    override fun showFeedbackDialog(project: Project) {
        SimpleUserFeedbackDialog(project).show()
    }
}