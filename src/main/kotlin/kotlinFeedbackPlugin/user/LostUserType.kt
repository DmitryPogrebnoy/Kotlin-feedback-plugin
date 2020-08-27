package kotlinFeedbackPlugin.user

import com.intellij.openapi.project.Project
import kotlinFeedbackPlugin.network.CustomQuestionsLoader.getLostCustomQuestion
import kotlinFeedbackPlugin.setting.FunctionalitySettings.enableNotification
import kotlinFeedbackPlugin.setting.FunctionalitySettings.requiredIdeaEapVersion
import kotlinFeedbackPlugin.show.isIntellijIdeaEAP
import kotlinFeedbackPlugin.ui.dialog.LostUserFeedbackDialog
import kotlinFeedbackPlugin.ui.notification.DontShowFeedbackNotificationAction
import kotlinFeedbackPlugin.ui.notification.LostUserTypeNotificationAction
import kotlinFeedbackPlugin.ui.notification.generateRequestFeedbackNotification

/**
 * This object represents the lost user type.
 *
 * @see kotlinFeedbackPlugin.user.UserType
 * @see kotlinFeedbackPlugin.user.UserTypeResolver
 */

object LostUserType : UserType {

    override val userTypeName: String = "Lost Kotlin user"
    override var customQuestion: CustomQuestion? = getLostCustomQuestion()

    override fun isUserSatisfiesUserType(): Boolean {
        return isSendFusEnabled() && checkDaysWithoutEditingKotlinFiles()
                && checkPreviousNumberEditingKotlinFiles()
    }

    override fun showFeedbackNotification(project: Project) {
        if (requiredIdeaEapVersion) {
            if (enableNotification && isIntellijIdeaEAP()) {
                val notification = generateRequestFeedbackNotification()
                notification.addAction(LostUserTypeNotificationAction())
                notification.addAction(DontShowFeedbackNotificationAction())
                notification.notify(project)
            }
        } else {
            if (enableNotification) {
                val notification = generateRequestFeedbackNotification()
                notification.addAction(LostUserTypeNotificationAction())
                notification.addAction(DontShowFeedbackNotificationAction())
                notification.notify(project)
            }
        }
    }

    override fun showFeedbackDialog(project: Project) {
        LostUserFeedbackDialog(project).show()
    }
}