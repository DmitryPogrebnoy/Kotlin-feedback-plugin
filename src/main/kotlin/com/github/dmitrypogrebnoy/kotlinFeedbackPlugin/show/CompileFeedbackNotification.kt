package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.show

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification.FeedbackNotification
import com.intellij.openapi.compiler.CompileContext
import com.intellij.openapi.compiler.CompileTask

class CompileFeedbackNotification : CompileTask {

    override fun execute(context: CompileContext?): Boolean {
        if (context != null) {
            if (checkCompileTaskDuration(context.project) && checkRelevantNumberEditing() &&
                    //Set '!' for test          //Set '!' for test
                    (!isIntellijIdeaEAP() || !isKotlinPluginEAP()) && checkFeedbackDate()
            ) {
                FeedbackNotification.showNotification(context.project)
            }
        }
        return true
    }
}