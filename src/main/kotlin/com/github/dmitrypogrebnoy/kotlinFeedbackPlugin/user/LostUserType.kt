package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.network.CustomQuestionsLoader.getLostCustomQuestion
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.setting.FunctionalitySettings.enableNotification
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.dialog.LostUserFeedbackDialog
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification.LostUserTypeNotificationAction
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification.RequestFeedbackNotification
import com.intellij.openapi.project.Project

/**
 * This object represents the lost user type.
 *
 * @see com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user.UserType
 * @see com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user.UserTypeResolver
 */

object LostUserType : UserType {

    override val userTypeName: String = "Lost Kotlin user"
    override var customQuestion: CustomQuestion? = getLostCustomQuestion()

    override fun isUserSatisfiesUserType(): Boolean {
        return needCollectUserFeedback()
                && checkDaysWithoutEditingKotlinFiles() && checkPreviousNumberEditingKotlinFiles()
    }

    override fun showFeedbackNotification(project: Project) {
        if (enableNotification) {
            val notification = RequestFeedbackNotification()
            notification.addAction(LostUserTypeNotificationAction())
            notification.notify(project)
        }
    }

    override fun showFeedbackDialog(project: Project) {
        LostUserFeedbackDialog(project).show()
    }
}