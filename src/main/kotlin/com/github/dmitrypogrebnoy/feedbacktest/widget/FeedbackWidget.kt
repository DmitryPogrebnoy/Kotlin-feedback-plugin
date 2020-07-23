package com.github.dmitrypogrebnoy.feedbacktest.widget

import com.github.dmitrypogrebnoy.feedbacktest.FeedbackBundle
import com.github.dmitrypogrebnoy.feedbacktest.dialog.FeedbackDialog
import com.intellij.codeInsight.hint.HintManager
import com.intellij.icons.AllIcons
import com.intellij.ide.util.EditorGotoLineNumberDialog
import com.intellij.ide.util.GotoLineNumberDialog
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.fileEditor.ex.IdeDocumentHistory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.impl.status.EditorBasedWidget
import com.intellij.ui.UIBundle
import com.intellij.ui.awt.RelativePoint
import com.intellij.util.Consumer
import java.awt.event.MouseEvent
import javax.swing.Icon

class FeedbackWidget(project: Project): EditorBasedWidget(project), StatusBarWidget.IconPresentation {

    companion object {
        const val ID = "feedbackWidget"
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
                        val dialog = FeedbackDialog(project)
                        dialog.show()
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