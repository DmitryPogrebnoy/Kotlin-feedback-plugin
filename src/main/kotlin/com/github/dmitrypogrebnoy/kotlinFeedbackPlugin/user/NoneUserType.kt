package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user

import com.intellij.openapi.project.Project

/**
 * This object represents the none user type.
 *
 * @see com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user.UserType
 * @see com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user.UserTypeResolver
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