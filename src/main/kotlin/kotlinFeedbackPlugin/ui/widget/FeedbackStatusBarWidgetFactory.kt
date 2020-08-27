package kotlinFeedbackPlugin.ui.widget

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory
import kotlinFeedbackPlugin.bundle.FeedbackBundle
import kotlinFeedbackPlugin.setting.FunctionalitySettings.enableWidget
import kotlinFeedbackPlugin.setting.FunctionalitySettings.requiredIdeaEapVersion
import kotlinFeedbackPlugin.show.isIntellijIdeaEAP
import kotlinFeedbackPlugin.user.NoneUserType
import kotlinFeedbackPlugin.user.UserTypeResolver.currentUserType

/**
 * Feedback plugin widget factory.
 *
 * @see kotlinFeedbackPlugin.ui.widget.FeedbackWidget
 */


/*
 * Set when the project window is created and is NOT updated when the statusBar.update Widget (id) is called.
 * Thus, the visibility and customization of the widget is only updated when the project is reopened.
 */
class FeedbackStatusBarWidgetFactory : StatusBarWidgetFactory {

    override fun getId(): String {
        return FeedbackWidget.ID
    }

    override fun isEnabledByDefault(): Boolean {
        return if (requiredIdeaEapVersion) {
            enableWidget && (currentUserType != NoneUserType) && isIntellijIdeaEAP()
        } else {
            enableWidget && (currentUserType != NoneUserType)
        }
    }

    override fun getDisplayName(): String {
        return FeedbackBundle.getMessage("widget.display.name")
    }

    override fun disposeWidget(widget: StatusBarWidget) {
        Disposer.dispose(widget)
    }

    override fun isAvailable(project: Project): Boolean {
        return if (requiredIdeaEapVersion) {
            enableWidget && (currentUserType != NoneUserType) && isIntellijIdeaEAP()
        } else {
            enableWidget && (currentUserType != NoneUserType)
        }
    }

    override fun createWidget(project: Project): StatusBarWidget {
        return FeedbackWidget(project)
    }

    override fun canBeEnabledOn(statusBar: StatusBar): Boolean {
        return if (requiredIdeaEapVersion) {
            enableWidget && (currentUserType != NoneUserType) && isIntellijIdeaEAP()
        } else {
            enableWidget && (currentUserType != NoneUserType)
        }
    }

    override fun isConfigurable(): Boolean {
        return if (requiredIdeaEapVersion) {
            enableWidget && (currentUserType != NoneUserType) && isIntellijIdeaEAP()
        } else {
            enableWidget && (currentUserType != NoneUserType)
        }
    }
}