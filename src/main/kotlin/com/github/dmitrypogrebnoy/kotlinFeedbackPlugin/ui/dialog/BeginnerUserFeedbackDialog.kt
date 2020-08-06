package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.dialog

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.bundle.FeedbackBundle
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.send.FeedbackSender
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.notification.SuccessSendFeedbackNotification
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user.SimpleUserType
import com.intellij.ide.ui.laf.darcula.ui.DarculaPanelUI
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.EditorTextField
import com.intellij.ui.components.JBLabel
import com.intellij.ui.layout.panel
import java.awt.event.ActionEvent
import java.io.File
import java.io.IOException
import java.time.LocalDate
import javax.swing.AbstractAction
import javax.swing.Action
import javax.swing.UIManager

class BeginnerUserFeedbackDialog(project: Project) : AbstractFeedbackDialog(project) {

    override val titleLabel: JBLabel
    override val sectionLabel: JBLabel
    override val feedbackLabel: JBLabel
    override val feedbackTextArea: EditorTextField
    override val feedbackDialogPanel: DialogPanel
    private val attachFileLabel: JBLabel
    private val attachFile: TextFieldWithBrowseButton
    override val successSendFeedbackNotification: SuccessSendFeedbackNotification

    init {
        setFieldsDialog()
        //TODO: Remove " BEGINNER "
        titleLabel = createTitleLabel(FeedbackBundle.message("dialog.default.content.title") + " BEGINNER ")
        sectionLabel = createSectionLabel(FeedbackBundle.message("dialog.default.content.section"))
        feedbackLabel = createFeedbackLabel(FeedbackBundle.message("dialog.default.content.description.label"))
        feedbackTextArea = createFeedbackTextArea(
                project,
                FeedbackBundle.message("dialog.default.content.description.textarea.placeholder")
        )
        feedbackLabel.labelFor = feedbackTextArea
        attachFileLabel = createAttachFileLabel(FeedbackBundle.message("dialog.default.content.attach.file.label"))
        attachFile = createAttachFileChooser(
                project,
                FeedbackBundle.message("dialog.default.content.attach.file.title"),
                FeedbackBundle.message("dialog.default.content.attach.file.description")
        )

        feedbackDialogPanel = createFeedbackDialogPanel()

        successSendFeedbackNotification = SuccessSendFeedbackNotification()

        super.init()
        startTrackingValidation()
    }

    override fun setFieldsDialog() {
        title = FeedbackBundle.message("dialog.default.title")
        myOKAction = sendFeedbackAction()
        setOKButtonText(FeedbackBundle.message("dialog.default.button.ok"))
    }

    override fun sendFeedbackAction(): Action {
        return object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent?) {
                //first close window
                if (doValidateAll().isEmpty()) {
                    if (dateFeedbackStatService.state != null) {
                        dateFeedbackStatService.state!!.dateSendFeedback = LocalDate.now()
                    }
                    close(OK_EXIT_CODE)
                    successSendFeedbackNotification.notify(project)

                    //then try to send feedback
                    try {
                        val newIssueId = FeedbackSender.createFeedbackIssue(
                                SimpleUserType.userTypeName + "feedback",
                                feedbackTextArea.text
                        )
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
    }

    override fun createFeedbackDialogPanel(): DialogPanel {
        val dialogPanel = panel {
            row {
                titleLabel()
            }
            row {
                sectionLabel()
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
            preferredFocusedComponent = feedbackTextArea
        }
    }

    override fun doValidateAll(): MutableList<ValidationInfo> {
        val validationInfoList = mutableListOf<ValidationInfo>()

        val validationAttachFile = checkAttachFile()
        if (validationAttachFile != null) {
            validationInfoList.add(validationAttachFile)
        }

        if (feedbackTextArea.text.isEmpty()) {
            validationInfoList.add(ValidationInfo(FeedbackBundle.message("dialog.default.validate.description.empty"), feedbackTextArea))
        }

        return validationInfoList
    }

    private fun checkAttachFile(): ValidationInfo? {
        if (attachFile.text.isNotEmpty()) {
            val file = File(attachFile.text)
            if (!file.exists()) {
                return ValidationInfo(FeedbackBundle.message("dialog.default.validate.attach.file.not.exists"), attachFile)
            } else {
                val fileSize = file.length()
                if (fileSize > FeedbackSender.attachFileMaxSize) {
                    return ValidationInfo(FeedbackBundle.message("dialog.default.validate.attach.file.too.large"), attachFile)
                }
            }
        }
        return null
    }
}