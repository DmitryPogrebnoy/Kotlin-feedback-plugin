package kotlinFeedbackPlugin.user

import com.intellij.openapi.project.Project
import kotlinFeedbackPlugin.network.CustomQuestionsLoader.getBeginnerCustomQuestion
import kotlinFeedbackPlugin.setting.FunctionalitySettings.enableNotification
import kotlinFeedbackPlugin.setting.FunctionalitySettings.requiredIdeaEapVersion
import kotlinFeedbackPlugin.show.isIntellijIdeaEAP
import kotlinFeedbackPlugin.ui.dialog.BeginnerUserFeedbackDialog
import kotlinFeedbackPlugin.ui.notification.BeginnerUserTypeNotificationAction
import kotlinFeedbackPlugin.ui.notification.DontShowFeedbackNotificationAction
import kotlinFeedbackPlugin.ui.notification.generateRequestFeedbackNotification

/**
 * This object represents the beginner user type.
 *
 * @see kotlinFeedbackPlugin.user.UserType
 * @see kotlinFeedbackPlugin.user.UserTypeResolver
 */

object BeginnerUserType : UserType {

    override val userTypeName: String = "Beginner Kotlin user"

    override var customQuestion: CustomQuestion? = getBeginnerCustomQuestion()

    override fun isUserSatisfiesUserType(): Boolean {
        return isSendFusEnabled() && checkRelevantNumberKotlinFileEditing()
                && (isEduToolsPluginEnabled() || openedManyRecentProjectsWithoutVcs())
    }

    override fun showFeedbackNotification(project: Project) {
        if (requiredIdeaEapVersion) {
            if (enableNotification && isIntellijIdeaEAP()) {
                val notification = generateRequestFeedbackNotification()
                notification.addAction(BeginnerUserTypeNotificationAction())
                notification.addAction(DontShowFeedbackNotificationAction())
                notification.notify(project)
            }
        } else {
            if (enableNotification) {
                val notification = generateRequestFeedbackNotification()
                notification.addAction(BeginnerUserTypeNotificationAction())
                notification.addAction(DontShowFeedbackNotificationAction())
                notification.notify(project)
            }
        }
    }

    override fun showFeedbackDialog(project: Project) {
        BeginnerUserFeedbackDialog(project).show()
    }
}