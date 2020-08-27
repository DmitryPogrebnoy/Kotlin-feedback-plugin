package kotlinFeedbackPlugin.user

import com.intellij.openapi.project.Project

/**
 * This object represents the none user type.
 *
 * @see kotlinFeedbackPlugin.user.UserType
 * @see kotlinFeedbackPlugin.user.UserTypeResolver
 */

object NoneUserType : UserType {

    override val userTypeName: String = "None Kotlin user"

    //Always null
    override var customQuestion: CustomQuestion? = null

    override fun isUserSatisfiesUserType(): Boolean {
        return true
    }

    // Do nothing
    override fun showFeedbackNotification(project: Project) {
    }

    // Do nothing
    override fun showFeedbackDialog(project: Project) {
    }
}