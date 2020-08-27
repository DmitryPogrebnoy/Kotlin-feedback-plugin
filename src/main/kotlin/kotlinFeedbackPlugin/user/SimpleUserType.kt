package kotlinFeedbackPlugin.user

import com.intellij.openapi.project.Project
import kotlinFeedbackPlugin.network.CustomQuestionsLoader.getSimpleCustomQuestion
import kotlinFeedbackPlugin.setting.FunctionalitySettings.enableNotification
import kotlinFeedbackPlugin.setting.FunctionalitySettings.requiredIdeaEapVersion
import kotlinFeedbackPlugin.show.isIntellijIdeaEAP
import kotlinFeedbackPlugin.ui.dialog.SimpleUserFeedbackDialog
import kotlinFeedbackPlugin.ui.notification.DontShowFeedbackNotificationAction
import kotlinFeedbackPlugin.ui.notification.SimpleUserTypeNotificationAction
import kotlinFeedbackPlugin.ui.notification.generateRequestFeedbackNotification

/**
 * This object represents the simple user type.
 *
 * @see kotlinFeedbackPlugin.user.UserType
 * @see kotlinFeedbackPlugin.user.UserTypeResolver
 */

object SimpleUserType : UserType {

    override val userTypeName: String = "Simple Kotlin user"

    override var customQuestion: CustomQuestion? = getSimpleCustomQuestion()

    override fun isUserSatisfiesUserType(): Boolean {
        return isSendFusEnabled() && checkRelevantNumberKotlinFileEditing()
    }

    override fun showFeedbackNotification(project: Project) {
        if (requiredIdeaEapVersion) {
            if (enableNotification && isIntellijIdeaEAP()) {
                val notification = generateRequestFeedbackNotification()
                notification.addAction(SimpleUserTypeNotificationAction())
                notification.addAction(DontShowFeedbackNotificationAction())
                notification.notify(project)
            }
        } else {
            if (enableNotification) {
                val notification = generateRequestFeedbackNotification()
                notification.addAction(SimpleUserTypeNotificationAction())
                notification.addAction(DontShowFeedbackNotificationAction())
                notification.notify(project)
            }
        }
    }

    override fun showFeedbackDialog(project: Project) {
        SimpleUserFeedbackDialog(project).show()
    }
}