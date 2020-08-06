package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user

import com.intellij.openapi.project.Project

object UserTypeResolver {

    private fun resolveUserType(): UserType {
        //TODO: Leave a comment why this order
        return when {
            BeginnerUserType.isUserSatisfiesUserType() -> BeginnerUserType
            ActiveUserType.isUserSatisfiesUserType() -> ActiveUserType
            SimpleUserType.isUserSatisfiesUserType() -> SimpleUserType
            else -> NoneUserType
        }
    }

    fun showFeedbackNotification(project: Project) {
        resolveUserType().showFeedbackNotification(project)
    }

    fun showFeedbackDialog(project: Project) {
        resolveUserType().showFeedbackNotification(project)
    }
}