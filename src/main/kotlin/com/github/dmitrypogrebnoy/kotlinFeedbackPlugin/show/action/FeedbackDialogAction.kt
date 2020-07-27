package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.show.action

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.dialog.FeedbackDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class FeedbackDialogAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        FeedbackDialog(e.project ?: return).show()
    }
}