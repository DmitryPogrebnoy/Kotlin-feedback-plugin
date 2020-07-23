package com.github.dmitrypogrebnoy.feedbacktest.widget

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory

class FeedbackStatusBarWidgetFactory: StatusBarWidgetFactory {
    override fun getId(): String {
        return FeedbackWidget.ID
    }

    override fun isEnabledByDefault(): Boolean {
        return true
    }

    override fun getDisplayName(): String {
        return "Share Kotlin Feedback"
    }

    override fun disposeWidget(widget: StatusBarWidget) {
        Disposer.dispose(widget)
    }

    override fun isAvailable(project: Project): Boolean {
        return true
    }

    override fun createWidget(project: Project): StatusBarWidget {
        return FeedbackWidget(project)
    }

    override fun canBeEnabledOn(statusBar: StatusBar): Boolean {
        return true
    }
}