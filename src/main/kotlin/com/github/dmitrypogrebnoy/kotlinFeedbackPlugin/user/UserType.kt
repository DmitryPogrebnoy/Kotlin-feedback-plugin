package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user

import com.intellij.openapi.project.Project

interface UserType {

    val userTypeName: String

    var customQuestion: CustomQuestion?

    fun isUserSatisfiesUserType(): Boolean

    fun showFeedbackNotification(project: Project)

    fun showFeedbackDialog(project: Project)
}