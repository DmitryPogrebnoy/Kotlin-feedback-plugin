package kotlinFeedbackPlugin.user

import com.intellij.openapi.project.Project
import kotlinFeedbackPlugin.network.CustomQuestionsLoader.getActiveCustomQuestion
import kotlinFeedbackPlugin.setting.FunctionalitySettings.enableNotification
import kotlinFeedbackPlugin.setting.FunctionalitySettings.requiredIdeaEapVersion
import kotlinFeedbackPlugin.show.isIntellijIdeaEAP
import kotlinFeedbackPlugin.ui.dialog.ActiveUserFeedbackDialog
import kotlinFeedbackPlugin.ui.notification.ActiveUserTypeNotificationAction
import kotlinFeedbackPlugin.ui.notification.DontShowFeedbackNotificationAction
import kotlinFeedbackPlugin.ui.notification.generateRequestFeedbackNotification

/**
 * This object represents the active user type.
 *
 * @see kotlinFeedbackPlugin.user.UserType
 * @see kotlinFeedbackPlugin.user.UserTypeResolver
 */

object ActiveUserType : UserType {

    override val userTypeName: String = "Active Kotlin user"

    override var customQuestion: CustomQuestion? = getActiveCustomQuestion()

    override fun isUserSatisfiesUserType(): Boolean {
        return isSendFusEnabled() && isKotlinPluginEAP()
                && checkRelevantNumberKotlinFileEditing()
    }

    override fun showFeedbackNotification(project: Project) {
        if (requiredIdeaEapVersion) {
            if (enableNotification && isIntellijIdeaEAP()) {
                val notification = generateRequestFeedbackNotification()
                notification.addAction(ActiveUserTypeNotificationAction())
                notification.addAction(DontShowFeedbackNotificationAction())
                notification.notify(project)
            }
        } else {
            if (enableNotification) {
                val notification = generateRequestFeedbackNotification()
                notification.addAction(ActiveUserTypeNotificationAction())
                notification.addAction(DontShowFeedbackNotificationAction())
                notification.notify(project)
            }
        }
    }

    override fun showFeedbackDialog(project: Project) {
        ActiveUserFeedbackDialog(project).show()
    }
}