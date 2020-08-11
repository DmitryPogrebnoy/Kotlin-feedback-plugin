package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.show

import com.intellij.openapi.compiler.CompileContext
import com.intellij.openapi.compiler.CompileTask

/**
 * CompileTask for showing feedback notification.
 */

class CompileFeedbackNotification : CompileTask {

    override fun execute(context: CompileContext?): Boolean {
        if (context != null) {
            showCompileTimeNotificationIfPossible(context.project)
        }
        return true
    }
}