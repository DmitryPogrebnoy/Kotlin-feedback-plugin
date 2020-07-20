package com.github.dmitrypogrebnoy.feedbacktest.dialog

import com.github.dmitrypogrebnoy.feedbacktest.FeedbackBundle.message
import com.intellij.ide.ui.laf.darcula.ui.*
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.*
import com.intellij.ui.DocumentAdapter
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextArea
import com.intellij.ui.components.JBTextField
import com.intellij.ui.layout.panel
import java.awt.Dimension
import java.awt.Font
import java.awt.event.ActionEvent
import java.io.File
import java.io.IOException
import java.util.*
import java.util.function.Supplier
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import javax.swing.AbstractAction
import javax.swing.JComponent
import javax.swing.event.DocumentEvent


class FeedbackDialog(project: Project) : DialogWrapper(project) {

    private val regularFont: Font = Font("Segoe UI", Font.PLAIN, 12)

    private val titleLabel: JBLabel
    private val sectionLabel: JBLabel
    private val subjectLabel: JBLabel
    private val descriptionLabel: JBLabel
    private val subjectTextField: JBTextField
    private val descriptionTextArea: JBTextArea
    private val attachFileLabel: JBLabel
    private val attachFile: TextFieldWithBrowseButton

    private val feedbackDialogPanel: DialogPanel

    private val textFieldListener: javax.swing.event.DocumentListener

    var isSuccessSendMail: Boolean

    init {
        isSuccessSendMail = true
        setFieldsDialog()

        titleLabel = JBLabel(message("dialog.content.title"))
        titleLabel.ui = DarculaLabelUI()
        titleLabel.font = regularFont.deriveFont(Font.BOLD, 20F)

        sectionLabel = JBLabel(message("dialog.content.section"))
        sectionLabel.ui = DarculaLabelUI()
        sectionLabel.font = regularFont

        subjectLabel = JBLabel(message("dialog.content.subject"))
        subjectLabel.ui = DarculaLabelUI()
        subjectLabel.font = regularFont.deriveFont(Font.BOLD)

        subjectTextField = JBTextField()
        subjectTextField.ui = DarculaTextFieldUI()
        subjectTextField.font = regularFont
        subjectTextField.border = DarculaTextBorder()
        subjectTextField.preferredSize = Dimension(200, 20)
        subjectLabel.labelFor = subjectTextField

        descriptionLabel = JBLabel(message("dialog.content.description.label"))
        descriptionLabel.ui = DarculaLabelUI()
        descriptionLabel.font = regularFont.deriveFont(Font.BOLD)

        descriptionTextArea = JBTextArea()
        descriptionTextArea.ui = DarculaTextAreaUI()
        descriptionTextArea.font = regularFont
        descriptionTextArea.autoscrolls = true
        descriptionTextArea.rows = 7
        descriptionTextArea.preferredSize = Dimension(700, 50)
        //TODO: Set margin - too difficult omg
        // The text area gets out of the label for this reason:
        // https://stackoverflow.com/questions/8792651/how-can-i-add-padding-to-a-jtextfield/8792905
        /*
        descriptionTextArea.border = BorderFactory.createCompoundBorder(
                 descriptionTextArea.border, DarculaTextBorder()
        )
        //descriptionTextArea.margin = Insets(4, 6, 4, 6)
        */
        //descriptionTextArea.border = DarculaTextBorder()
        descriptionTextArea.lineWrap = true
        descriptionTextArea.wrapStyleWord = true
        descriptionTextArea.emptyText.text = message("dialog.content.description.textarea.placeholder")
        descriptionLabel.labelFor = descriptionTextArea

        attachFileLabel = JBLabel(message("dialog.content.attachfile.label"))
        attachFileLabel.ui = DarculaLabelUI()
        attachFileLabel.font = regularFont.deriveFont(Font.BOLD)

        attachFile = TextFieldWithBrowseButton()
        attachFile.addBrowseFolderListener(
                message("dialog.content.attachfile.title"),
                message("dialog.content.attachfile.description"),
                project,
                FileChooserDescriptorFactory.createMultipleFilesNoJarsDescriptor()
        )

        //Add "file exists" validator to attach file
        ComponentValidator(disposable).withValidator(
                Supplier<ValidationInfo> {
                    if (attachFile.text.isNotEmpty()) {
                        val file = File(attachFile.text)
                        if (!file.exists()) {
                            ValidationInfo(message("dialog.validate.attachFile"), attachFile)
                        } else {
                            ValidationInfo("", null)
                        }
                    } else {
                        ValidationInfo("", null)
                    }
                }).andStartOnFocusLost().installOn(attachFile)

        attachFile.textField.document.addDocumentListener(object : DocumentAdapter() {
            override fun textChanged(e: DocumentEvent) {
                ComponentValidator.getInstance(attachFile).ifPresent { v: ComponentValidator -> v.revalidate() }
            }
        })

        textFieldListener = object : javax.swing.event.DocumentListener {
            override fun changedUpdate(e: DocumentEvent?) {
                checkNotEmptyField()
            }

            override fun insertUpdate(e: DocumentEvent?) {
                checkNotEmptyField()
            }

            override fun removeUpdate(e: DocumentEvent?) {
                checkNotEmptyField()
            }
        }

        subjectTextField.document.addDocumentListener(textFieldListener)
        descriptionTextArea.document.addDocumentListener(textFieldListener)

        feedbackDialogPanel = panel {
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
                cell(true, true) {
                    descriptionLabel()
                    descriptionTextArea()
                }
                cell(true, true) {
                    attachFileLabel()
                    attachFile()
                }
            }
        }

        feedbackDialogPanel.ui = DarculaPanelUI()
        feedbackDialogPanel.font = regularFont
        feedbackDialogPanel.preferredFocusedComponent = subjectTextField

        super.init()
    }

    private fun setFieldsDialog() {
        title = message("dialog.title")

        myOKAction = object : AbstractAction() {
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
                    val attachmentPart = MimeBodyPart()
                    val textPart = MimeBodyPart()
                    try {
                        val f = File(attachFile.text)
                        attachmentPart.attachFile(f)
                        textPart.setText(descriptionTextArea.text)
                        multipart.addBodyPart(textPart)
                        multipart.addBodyPart(attachmentPart)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    message.setContent(multipart)
                    message.setFrom(InternetAddress(from))
                    message.addRecipient(Message.RecipientType.TO, InternetAddress(to))
                    message.subject = subjectTextField.text
                    Transport.send(message)
                    isSuccessSendMail = true
                    startTrackingValidation()

                } catch (mex: MessagingException) {
                    isSuccessSendMail = false
                    startTrackingValidation()
                    checkNotEmptyField()
                    mex.printStackTrace()
                }

                if (doValidateAll().isEmpty()) {
                    close(OK_EXIT_CODE)
                }
            }
        }

        setOKButtonText(message("dialog.button.ok"))
        isOKActionEnabled = false
    }

    override fun createCenterPanel(): JComponent? {
        return feedbackDialogPanel
    }

    private fun checkNotEmptyField() {
        isOKActionEnabled = subjectTextField.text.isNotEmpty() && descriptionTextArea.text.isNotEmpty()
    }

    override fun doValidateAll(): MutableList<ValidationInfo> {
        val validationInfoList = mutableListOf<ValidationInfo>()

        if (!isSuccessSendMail) {
            validationInfoList.add(ValidationInfo(message("dialog.validate.all.exceptionSendMail")))
        }

        val file = File(attachFile.text)
        if (!file.exists()) {
            validationInfoList.add(ValidationInfo(message("dialog.validate.all.attachFile")))
        }

        return validationInfoList
    }
}