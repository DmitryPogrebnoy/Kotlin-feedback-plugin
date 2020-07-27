package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.dialog

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.bundle.FeedbackBundle.message
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.DateFeedbackStatService
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
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import javax.swing.*


class FeedbackDialog(project: Project) : DialogWrapper(project) {

    companion object {
        //25 MB in Byte
        const val MAX_ATTACH_FILE_SIZE = 26214400
    }

    private val titleLabel: JBLabel
    private val sectionLabel: JBLabel
    private val subjectLabel: JBLabel
    private val feedbackLabel: JBLabel
    private val subjectTextField: EditorTextField
    private val feedbackTextArea: EditorTextField
    private val attachFileLabel: JBLabel
    private val attachFile: TextFieldWithBrowseButton

    private val feedbackDialogPanel: DialogPanel

    private var isSuccessSendFeedback: Boolean

    private val dateFeedbackStatService: DateFeedbackStatService = service()

    init {
        setFieldsDialog()

        isSuccessSendFeedback = true

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
                cell(true, true) {
                    subjectLabel()
                    subjectTextField()
                }
            }
            row {
                cell(true, true) {
                    feedbackLabel()
                    feedbackTextArea()
                }
            }
            row {
                cell(true, true) {
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
                //TODO:Remove email
                val email = ""
                val to: String = email
                val from: String = email
                val host = "smtp.gmail.com"
                val properties: Properties = System.getProperties()
                properties["mail.smtp.host"] = host
                properties["mail.smtp.port"] = "465"
                properties["mail.smtp.ssl.enable"] = "true"
                properties["mail.smtp.auth"] = "true"
                val session: Session = Session.getInstance(properties, object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        //TODO:Remove email and password
                        return PasswordAuthentication("", "")
                    }
                })

                session.debug = true

                try {
                    val message = MimeMessage(session)
                    val multipart: Multipart = MimeMultipart()
                    val textPart = MimeBodyPart()
                    textPart.setText(feedbackTextArea.text)
                    multipart.addBodyPart(textPart)
                    if (attachFile.text.isNotEmpty()) {
                        val attachmentPart = MimeBodyPart()
                        val f = File(attachFile.text)
                        attachmentPart.attachFile(f)
                        multipart.addBodyPart(attachmentPart)
                    }
                    message.setContent(multipart)
                    message.setFrom(InternetAddress(from))
                    message.addRecipient(Message.RecipientType.TO, InternetAddress(to))
                    message.subject = subjectTextField.text
                    Transport.send(message)
                    isSuccessSendFeedback = true
                } catch (e: IOException) {
                    isSuccessSendFeedback = false
                    e.printStackTrace()
                }

                if (doValidateAll().isEmpty()) {
                    if (dateFeedbackStatService.state != null) {
                        dateFeedbackStatService.state!!.sendFeedbackDate = LocalDate.now()
                    }
                    close(OK_EXIT_CODE)
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

        if (!isSuccessSendFeedback) {
            validationInfoList.add(ValidationInfo(message("dialog.validate.exception.send.mail")))
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
                if (fileSize > MAX_ATTACH_FILE_SIZE) {
                    return ValidationInfo(message("dialog.validate.attach.file.too.large"), attachFile)
                }
            }
        }
        return null
    }
}