package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user

import com.intellij.openapi.project.Project

/**
 * Basic interface for all user types
 *
 * @see com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user.UserTypeResolver
 */

interface UserType {

    val userTypeName: String

    var customQuestion: CustomQuestion?

    fun isUserSatisfiesUserType(): Boolean

    fun showFeedbackNotification(project: Project)

    fun showFeedbackDialog(project: Project)
}