package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.dialog

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.bundle.FeedbackBundle.message
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.send.FeedbackSender
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.DateFeedbackStatService
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification.SuccessSendFeedbackNotification
import com.intellij.ide.ui.laf.darcula.ui.DarculaLabelUI
import com.intellij.ide.ui.laf.darcula.ui.DarculaPanelUI
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
import com.intellij.ui.layout.panel
import java.awt.Dimension
import java.awt.Font
import java.awt.event.ActionEvent
import java.io.File
import java.io.IOException
import java.time.LocalDate
import javax.swing.*


class FeedbackDialog(private val project: Project) : DialogWrapper(project) {

    private val titleLabel: JBLabel
    private val sectionLabel: JBLabel
    private val subjectLabel: JBLabel
    private val feedbackLabel: JBLabel
    private val subjectTextField: EditorTextField
    private val feedbackTextArea: EditorTextField
    private val attachFileLabel: JBLabel
    private val attachFile: TextFieldWithBrowseButton
    private val feedbackDialogPanel: DialogPanel
    private val dateFeedbackStatService: DateFeedbackStatService = service()
    private val successSendFeedbackNotification: SuccessSendFeedbackNotification

    init {
        setFieldsDialog()
        titleLabel = createTitleLabel()
        sectionLabel = createSectionLabel()
        subjectLabel = createSubjectLabel()
        subjectTextField = createSubjectTextField(project)
        subjectLabel.labelFor = subjectTextField
        feedbackLabel = createFeedbackLabel()
        feedbackTextArea = createFeedbackTextArea(project)
        feedbackLabel.labelFor = feedbackTextArea
        attachFileLabel = createAttachFileLabel()
        attachFile = createAttachFileChooser(project)

        feedbackDialogPanel = createFeedbackDialogPanel()

        successSendFeedbackNotification = SuccessSendFeedbackNotification(project)

        super.init()
        startTrackingValidation()
    }

    private fun setFieldsDialog() {
        title = message("dialog.title")
        myOKAction = createOkAction()
        setOKButtonText(message("dialog.button.ok"))
        isOKActionEnabled = false
    }

    private fun createTitleLabel(): JBLabel {
        return JBLabel(message("dialog.content.title")).apply {
            ui = DarculaLabelUI()
            font = UIManager.getFont("Label.font").deriveFont(Font.BOLD, 20F)
        }
    }

    private fun createSectionLabel(): JBLabel {
        return JBLabel(message("dialog.content.section")).apply {
            ui = DarculaLabelUI()
            font = UIManager.getFont("Label.font")
        }
    }

    private fun createSubjectLabel(): JBLabel {
        return JBLabel(message("dialog.content.subject")).apply {
            ui = DarculaLabelUI()
            font = UIManager.getFont("Label.font").deriveFont(Font.BOLD)
        }
    }

    private fun createSubjectTextField(project: Project): EditorTextField {
        return EditorTextField(project, PlainTextFileType.INSTANCE).apply {
            preferredSize = Dimension(700, 20)
        }
    }

    private fun createFeedbackLabel(): JBLabel {
        return JBLabel(message("dialog.content.description.label")).apply {
            ui = DarculaLabelUI()
            font = UIManager.getFont("Label.font").deriveFont(Font.BOLD)
        }
    }

    private fun createFeedbackTextArea(project: Project): EditorTextField {
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
            setPlaceholder(message("dialog.content.description.textarea.placeholder"))
            setOneLineMode(false)
            preferredSize = Dimension(700, 200)
        }
    }

    private fun createAttachFileLabel(): JBLabel {
        return JBLabel(message("dialog.content.attach.file.label")).apply {
            ui = DarculaLabelUI()
            font = UIManager.getFont("Label.font").deriveFont(Font.BOLD)
        }
    }

    private fun createAttachFileChooser(project: Project): TextFieldWithBrowseButton {
        return TextFieldWithBrowseButton().apply {
            addBrowseFolderListener(
                    message("dialog.content.attach.file.title"),
                    message("dialog.content.attach.file.description"),
                    project,
                    FileChooserDescriptorFactory.createMultipleFilesNoJarsDescriptor()
            )
            preferredSize = Dimension(700, 20)
        }
    }

    private fun createFeedbackDialogPanel(): DialogPanel {
        val dialogPanel = panel {
            row {
                titleLabel()
            }
            row {
                sectionLabel()
            }
            row {
                cell(isVerticalFlow = true, isFullWidth = true) {
                    subjectLabel()
                    subjectTextField()
                }
            }
            row {
                cell(isVerticalFlow = true, isFullWidth = true) {
                    feedbackLabel()
                    feedbackTextArea()
                }
            }
            row {
                cell(isVerticalFlow = true, isFullWidth = true) {
                    attachFileLabel()
                    attachFile()
                }
            }
        }

        return dialogPanel.apply {
            ui = DarculaPanelUI()
            font = UIManager.getFont("Label.font")
            preferredFocusedComponent = subjectTextField
        }
    }

    private fun createOkAction(): Action {
        return object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent?) {
                //first close window
                if (doValidateAll().isEmpty()) {
                    if (dateFeedbackStatService.state != null) {
                        dateFeedbackStatService.state!!.sendFeedbackDate = LocalDate.now()
                    }
                    close(OK_EXIT_CODE)
                    SuccessSendFeedbackNotification(project).justNotify()
                }

                //then try to send feedback
                try {
                    val newIssueId = FeedbackSender.createFeedbackIssue(subjectTextField.text, feedbackTextArea.text)
                    if (attachFile.text.isNotEmpty()) {
                        val file = File(attachFile.text)
                        FeedbackSender.attachFileToIssue(newIssueId, file)
                    }
                } catch (e: IOException) {
                    //TODO: Implement send feedback later if exception is occurred
                    println(e.message)
                    e.printStackTrace()
                }
            }
        }
    }

    override fun createCenterPanel(): JComponent? {
        return feedbackDialogPanel
    }

    override fun doValidateAll(): MutableList<ValidationInfo> {
        val validationInfoList = mutableListOf<ValidationInfo>()

        val validationAttachFile = checkAttachFile()
        if (validationAttachFile != null) {
            validationInfoList.add(validationAttachFile)
        }

        if (subjectTextField.text.isEmpty()) {
            validationInfoList.add(ValidationInfo(message("dialog.validate.subject.empty"), subjectTextField))
        }

        if (feedbackTextArea.text.isEmpty()) {
            validationInfoList.add(ValidationInfo(message("dialog.validate.description.empty"), feedbackTextArea))
        }

        return validationInfoList
    }

    private fun checkAttachFile(): ValidationInfo? {
        if (attachFile.text.isNotEmpty()) {
            val file = File(attachFile.text)
            if (!file.exists()) {
                return ValidationInfo(message("dialog.validate.attach.file.not.exists"), attachFile)
            } else {
                val fileSize = file.length()
                if (fileSize > FeedbackSender.attachFileMaxSize) {
                    return ValidationInfo(message("dialog.validate.attach.file.too.large"), attachFile)
                }
            }
        }
        return null
    }
}