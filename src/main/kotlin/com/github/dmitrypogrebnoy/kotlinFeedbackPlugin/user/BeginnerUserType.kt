package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.network.CustomQuestionsLoader.getBeginnerCustomQuestion
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.setting.FunctionalitySettings.enableNotification
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.dialog.BeginnerUserFeedbackDialog
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification.BeginnerUserTypeNotificationAction
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification.RequestFeedbackNotification
import com.intellij.openapi.project.Project

/**
 * This object represents the beginner user type.
 *
 * @see com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user.UserType
 * @see com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user.UserTypeResolver
 */

object BeginnerUserType : UserType {

    override val userTypeName: String = "Beginner Kotlin user"

    override var customQuestion: CustomQuestion? = getBeginnerCustomQuestion()

    override fun isUserSatisfiesUserType(): Boolean {
        return needCollectUserFeedback()
                && isKotlinPluginEnabled()
                && checkRelevantNumberKotlinFileEditing()
                && (isEduToolsPluginEnabled() || openedManyRecentProjectsWithoutVcs())
    }

    override fun showFeedbackNotification(project: Project) {
        if (enableNotification) {
            val notification = RequestFeedbackNotification()
            notification.addAction(BeginnerUserTypeNotificationAction())
            notification.notify(project)
        }
    }

    override fun showFeedbackDialog(project: Project) {
        BeginnerUserFeedbackDialog(project).show()
    }
}