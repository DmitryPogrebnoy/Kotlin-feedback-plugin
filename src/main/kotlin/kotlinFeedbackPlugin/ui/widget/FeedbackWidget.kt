package kotlinFeedbackPlugin.ui.widget

import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.WindowManager
import com.intellij.util.Consumer
import kotlinFeedbackPlugin.bundle.FeedbackBundle
import kotlinFeedbackPlugin.setting.FunctionalitySettings.enableWidgetIconColor
import kotlinFeedbackPlugin.show.checkFeedbackDatesForWidget
import kotlinFeedbackPlugin.user.UserTypeResolver.currentUserType
import java.awt.event.MouseEvent
import javax.swing.Icon

/**
 * Feedback plugin widget.
 *
 * @see kotlinFeedbackPlugin.ui.widget.FeedbackStatusBarWidgetFactory
 */

/*
 * Set when the project window is created and is updated when the statusBar.update Widget (id) is called.
 * Thus, widget updating when DataFeedbackState changed. See DataFeedbackState for details.
 */
class FeedbackWidget(private val project: Project) : StatusBarWidget, StatusBarWidget.IconPresentation {

    companion object {
        const val ID = "Kotlin Feedback Widget"

        fun updateFeedbackWidgets() {
            ProjectManager.getInstance().openProjects.forEach {
                WindowManager.getInstance().getStatusBar(it).updateWidget(ID)
            }
        }
    }

    override fun ID(): String {
        return ID
    }

    override fun getTooltipText(): String? {
        return FeedbackBundle.message("widget.display.tooltip.text")
    }

    override fun getIcon(): Icon? {
        // Update widget when change DateFeedbackState
        return if (checkFeedbackDatesForWidget() && enableWidgetIconColor) {
            IconLoader.getIcon("/kotlin.svg")
        } else {
            IconLoader.getIcon("/kotlin-mono.svg")
        }
    }

    override fun getClickConsumer(): Consumer<MouseEvent>? {
        return Consumer {
            CommandProcessor.getInstance().executeCommand(
                    project,
                    {
                        currentUserType.showFeedbackDialog(project)
                    },
                    FeedbackBundle.message("widget.show.dialog.command.name"),
                    null
            )
        }
    }

    override fun getPresentation(): StatusBarWidget.WidgetPresentation? {
        return this
    }

    override fun install(statusBar: StatusBar) {
        // Widget tooltipText not displayed without widget update
        statusBar.updateWidget(ID)
    }

    override fun dispose() {
    }
}