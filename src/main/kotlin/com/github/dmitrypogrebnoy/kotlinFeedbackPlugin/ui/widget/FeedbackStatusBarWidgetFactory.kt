package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.widget

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.bundle.FeedbackBundle
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.setting.FunctionalitySettings.enableWidget
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user.NoneUserType
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user.UserType
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user.UserTypeResolver
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory

/**
 * Feedback plugin widget factory.
 *
 * @see com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.widget.FeedbackWidget
 */


/*
 * Set when the project window is created and is NOT updated when the statusBar.update Widget (id) is called.
 * Thus, the visibility and customization of the widget is only updated when the project is reopened.
 */
class FeedbackStatusBarWidgetFactory : StatusBarWidgetFactory {

    private val currentUserType: UserType = UserTypeResolver.resolveUserType()

    override fun getId(): String {
        return FeedbackWidget.ID
    }

    override fun isEnabledByDefault(): Boolean {
        return enableWidget && (currentUserType != NoneUserType)
    }

    override fun getDisplayName(): String {
        return FeedbackBundle.getMessage("widget.display.name")
    }

    override fun disposeWidget(widget: StatusBarWidget) {
        Disposer.dispose(widget)
    }

    override fun isAvailable(project: Project): Boolean {
        return enableWidget && (currentUserType != NoneUserType)
    }

    override fun createWidget(project: Project): StatusBarWidget {
        return FeedbackWidget(currentUserType, project)
    }

    override fun canBeEnabledOn(statusBar: StatusBar): Boolean {
        return enableWidget && (currentUserType != NoneUserType)
    }

    override fun isConfigurable(): Boolean {
        return enableWidget && (currentUserType != NoneUserType)
    }
}