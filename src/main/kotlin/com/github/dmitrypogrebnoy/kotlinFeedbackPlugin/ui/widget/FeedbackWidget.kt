package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.widget

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.bundle.FeedbackBundle
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user.UserTypeResolver
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.impl.status.EditorBasedWidget
import com.intellij.util.Consumer
import java.awt.event.MouseEvent
import javax.swing.Icon

//TODO:Update widget
class FeedbackWidget(project: Project): EditorBasedWidget(project), StatusBarWidget.IconPresentation {

    companion object {
        const val ID = "KotlinFeedbackWidget"
    }

    override fun ID(): String {
        return ID
    }

    override fun getTooltipText(): String? {
        return "Share Kotlin Feedback"
    }

    override fun getIcon(): Icon? {
        return IconLoader.getIcon("/kotlin.svg")
    }

    override fun getClickConsumer(): Consumer<MouseEvent>? {
        return Consumer {
            CommandProcessor.getInstance().executeCommand(
                    project,
                    {
                        UserTypeResolver.showFeedbackDialog(project)
                    },
                    FeedbackBundle.message("widget.show.dialog.command.name"),
                    null
            )
        }
    }

    override fun getPresentation(): StatusBarWidget.WidgetPresentation? {
        return this
    }
}