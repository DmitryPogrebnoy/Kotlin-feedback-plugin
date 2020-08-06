package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.dialog

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.bundle.FeedbackBundle
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.DateFeedbackStatService
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification.SuccessSendFeedbackNotification
import com.intellij.ide.ui.laf.darcula.ui.DarculaLabelUI
import com.intellij.openapi.components.service
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.EditorTextField
import com.intellij.ui.components.JBLabel
import java.awt.Dimension
import java.awt.Font
import javax.swing.Action
import javax.swing.BorderFactory
import javax.swing.JComponent
import javax.swing.UIManager

abstract class AbstractFeedbackDialog(protected val project: Project) : DialogWrapper(project) {

    protected abstract val titleLabel: JBLabel
    protected abstract val sectionLabel: JBLabel
    protected abstract val feedbackLabel: JBLabel
    protected abstract val feedbackTextArea: EditorTextField
    protected abstract val feedbackDialogPanel: DialogPanel
    protected val dateFeedbackStatService: DateFeedbackStatService = service()
    protected abstract val successSendFeedbackNotification: SuccessSendFeedbackNotification

    init {
        addTrackingClose()
        myCancelAction = createCancelWithTrackingAction()
        setCancelButtonText(FeedbackBundle.message("dialog.default.button.cancel"))
    }

    override fun createCenterPanel(): JComponent? {
        return feedbackDialogPanel
    }

    protected open fun createTitleLabel(labelText: String): JBLabel {
        return JBLabel(labelText).apply {
            ui = DarculaLabelUI()
            font = UIManager.getFont("Label.font").deriveFont(Font.BOLD, 20F)
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
                                feedbackTextArea.border)
                )
                it.setVerticalScrollbarVisible(true)
            }
            autoscrolls = true
            setPlaceholder(placeHolderText)
            setOneLineMode(false)
            preferredSize = Dimension(700, 200)
        }
    }

    protected open fun createAttachFileLabel(labelText: String): JBLabel {
        return JBLabel(labelText).apply {
            ui = DarculaLabelUI()
            font = UIManager.getFont("Label.font").deriveFont(Font.BOLD)
        }
    }

    protected open fun createAttachFileChooser(project: Project, titleText: String, descriptionText: String): TextFieldWithBrowseButton {
        return TextFieldWithBrowseButton().apply {
            addBrowseFolderListener(
                    titleText,
                    descriptionText,
                    project,
                    FileChooserDescriptorFactory.createMultipleFilesNoJarsDescriptor()
            )
            preferredSize = Dimension(700, 20)
        }
    }

    protected abstract fun createFeedbackDialogPanel(): DialogPanel

    protected abstract fun sendFeedbackAction(): Action

    protected abstract fun setFieldsDialog()

    abstract override fun doValidateAll(): MutableList<ValidationInfo>

    private fun createCancelWithTrackingAction(): Action {
        return CancelDialogWindowTracker(this)
    }

    private fun addTrackingClose() {
        super.getWindow().addWindowListener(CloseDialogWindowTracker())
    }
}