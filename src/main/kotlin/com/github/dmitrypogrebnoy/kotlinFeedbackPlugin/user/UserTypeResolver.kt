package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user

import com.intellij.openapi.project.Project

/**
 * Resolves the user type for the current user.
 */

object UserTypeResolver {
    /**
     * The resolving is as follows:
     *
     *     All user types, except None user type,
     *     must satisfies the following conditions:
     *     - Collection of statistics usage functions is enabled
     *
     *     Beginner user type:
     *     - Recently frequently edited Kotlin files
     *     - And at least one condition of the following:
     *         - Installed and enabled EduTools plugin
     *         - Recently often opened Kotlin projects without VCS
     *
     *     Active user type:
     *     - Recently frequently edited Kotlin files
     *     - Installed EAP version of Kotlin plugin
     *
     *     Simple user type:
     *     - Recently frequently edited Kotlin files
     *
     *     Lost user type:
     *     - The user previously edited Kotlin
     *     - Recently, the user has not been editing Kotlin files
     *
     *     None user type:
     *     - No conditions
     *
     * The order is defined so that the user does not fall into the simple user type,
     * while satisfying the active user type.
     *
     * None user type is used for users who do not match more than one user type.
     * There is no need to collect feedback from such users.
     *
     */
    val currentUserType: UserType = resolveUserType()

    private fun resolveUserType(): UserType {

        return when {
            BeginnerUserType.isUserSatisfiesUserType() -> BeginnerUserType
            ActiveUserType.isUserSatisfiesUserType() -> ActiveUserType
            SimpleUserType.isUserSatisfiesUserType() -> SimpleUserType
            LostUserType.isUserSatisfiesUserType() -> LostUserType
            else -> NoneUserType
        }
    }

    fun showFeedbackNotification(project: Project) {
        currentUserType.showFeedbackNotification(project)
    }

    fun showFeedbackDialog(project: Project) {
        currentUserType.showFeedbackDialog(project)
    }
}