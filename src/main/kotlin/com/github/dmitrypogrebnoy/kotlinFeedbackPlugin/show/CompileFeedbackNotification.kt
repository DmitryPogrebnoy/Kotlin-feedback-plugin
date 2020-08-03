package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.show

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification.FeedbackNotification
import com.intellij.openapi.compiler.CompileContext
import com.intellij.openapi.compiler.CompileTask

class CompileFeedbackNotification : CompileTask {

    override fun execute(context: CompileContext?): Boolean {
        if (context != null) {
            if (canShowNotificationInCompileTime(context.project)) {
                FeedbackNotification(context.project).trackingNotify()
            }
        }
        return true
    }
}