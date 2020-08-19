package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.dialog

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.bundle.FeedbackBundle
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.FeedbackDatesService
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification.SuccessSendFeedbackNotification
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user.CustomQuestion
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user.UserType
import com.intellij.ide.ui.laf.darcula.ui.DarculaLabelUI
import com.intellij.openapi.components.service
import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.util.IconLoader
import com.intellij.ui.EditorTextField
import com.intellij.ui.components.JBLabel
import java.awt.Font
import javax.swing.Action
import javax.swing.BorderFactory
import javax.swing.JComponent
import javax.swing.UIManager

/**
 * Base class for user types feedback dialogs.
 */

abstract class AbstractFeedbackDialog(protected val project: Project) : DialogWrapper(project) {

    protected abstract val titleLabel: JBLabel
    protected abstract val sectionLabel: JBLabel
    protected abstract val firstFeedbackQuestionLabel: JBLabel
    protected abstract val firstFeedbackQuestionTextArea: EditorTextField
    protected abstract val secondFeedbackQuestionLabel: JBLabel
    protected abstract val secondFeedbackQuestionTextArea: EditorTextField
    protected abstract val thirdFeedbackQuestionLabel: JBLabel
    protected abstract val thirdFeedbackQuestionTextArea: EditorTextField
    protected abstract val feedbackDialogPanel: DialogPanel
    protected abstract val customQuestionLabel: JBLabel?
    protected abstract val customQuestionTextArea: EditorTextField?
    protected val feedbackDatesService: FeedbackDatesService = service()
    protected abstract val successSendFeedbackNotification: SuccessSendFeedbackNotification

    init {
        addTrackingClose()
        myCancelAction = createCancelWithTrackingAction()
        setCancelButtonText(FeedbackBundle.message("dialog.default.button.cancel"))
        title = FeedbackBundle.message("dialog.default.title")
    }

    override fun createCenterPanel(): JComponent? {
        return feedbackDialogPanel
    }

    protected open fun createTitleLabel(labelText: String): JBLabel {
        return JBLabel(labelText).apply {
            ui = DarculaLabelUI()
            font = UIManager.getFont("Label.font").deriveFont(Font.BOLD, 20F)
            //TODO: Set right icon
            icon = IconLoader.getIcon("/kotlin.svg")
        }
    }

    protected open fun createSectionLabel(labelText: String): JBLabel {
        return JBLabel(labelText).apply {
            ui = DarculaLabelUI()
            font = UIManager.getFont("Label.font")
        }
    }

    protected open fun createFeedbackLabel(labelText: String): JBLabel {
        return JBLabel(labelText).apply {
            ui = DarculaLabelUI()
            font = UIManager.getFont("Label.font").deriveFont(Font.BOLD)
        }
    }

    protected open fun createFeedbackTextArea(project: Project, placeHolderText: String): EditorTextField {
        return EditorTextField(project, PlainTextFileType.INSTANCE).apply {
            addSettingsProvider {
                it.settings.isUseSoftWraps = true
                it.setBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createEmptyBorder(4, 4, 4, 0),
                                this.border)
                )
                it.setVerticalScrollbarVisible(true)
            }
            autoscrolls = true
            setPlaceholder(placeHolderText)
            setOneLineMode(false)
        }
    }

    protected open fun createCustomQuestionLabel(customQuestion: CustomQuestion?): JBLabel? {
        return JBLabel(customQuestion?.question ?: return null).apply {
            ui = DarculaLabelUI()
            font = UIManager.getFont("Label.font").deriveFont(Font.BOLD)
        }
    }

    protected open fun createCustomQuestionTextField(customQuestion: CustomQuestion?): EditorTextField? {
        return if (customQuestion != null) {
            EditorTextField(project, PlainTextFileType.INSTANCE).apply {
                addSettingsProvider {
                    it.settings.isUseSoftWraps = true
                    it.setBorder(
                            BorderFactory.createCompoundBorder(
                                    BorderFactory.createEmptyBorder(4, 4, 4, 0),
                                    this.border)
                    )
                    it.setVerticalScrollbarVisible(true)
                }
                autoscrolls = true
                setPlaceholder(customQuestion.textFieldSettings.placeholder)
                setOneLineMode(false)
            }
        } else null
    }

    protected abstract fun createFeedbackDialogPanel(): DialogPanel

    protected abstract fun sendFeedbackAction(userType: UserType): Action

    abstract override fun doValidateAll(): MutableList<ValidationInfo>

    private fun createCancelWithTrackingAction(): Action {
        return CancelDialogWindowTracker(this)
    }

    private fun addTrackingClose() {
        super.getWindow().addWindowListener(CloseDialogWindowTracker())
    }
}