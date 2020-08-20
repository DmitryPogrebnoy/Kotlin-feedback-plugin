package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.dialog

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.bundle.FeedbackBundle
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.network.FeedbackSender
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user.UserType
import com.intellij.ide.ui.laf.darcula.ui.DarculaPanelUI
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.EditorTextField
import com.intellij.ui.components.JBLabel
import com.intellij.ui.layout.GrowPolicy
import com.intellij.ui.layout.panel
import java.awt.Dimension
import java.awt.Font
import java.awt.event.ActionEvent
import java.io.File
import java.time.LocalDate
import javax.swing.AbstractAction
import javax.swing.Action
import javax.swing.UIManager

/**
 * Class for user types feedback dialogs with an attached file.
 */

abstract class AttachedFileFeedbackDialog(project: Project) : AbstractFeedbackDialog(project) {

    protected abstract val attachFileLabel: JBLabel
    protected abstract val attachFile: TextFieldWithBrowseButton

    protected open fun createAttachFileLabel(labelText: String): JBLabel {
        return JBLabel(labelText).apply {
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
        }
    }

    override fun sendFeedbackAction(userType: UserType): Action {
        return object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent?) {
                //first close window
                if (doValidateAll().isEmpty()) {
                    if (feedbackDatesService.state != null) {
                        feedbackDatesService.state!!.dateSendFeedback = LocalDate.now()
                    }
                    close(OK_EXIT_CODE)
                    successSendFeedbackNotification.notify(project)

                    //then try to send feedback
                    try {
                        val newIssueId = FeedbackSender.createFeedbackIssue(
                                userType.userTypeName + " feedback",
                                getFormattedFeedback()
                        )
                        if (attachFile.text.isNotEmpty()) {
                            val file = File(attachFile.text)
                            FeedbackSender.attachFileToIssue(newIssueId, file)
                        }
                    } catch (e: RuntimeException) {
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
                firstFeedbackQuestionLabel()
            }
            row {
                firstFeedbackQuestionTextArea(this.grow, this.push)
            }
            row {
                secondFeedbackQuestionLabel()
            }
            row {
                secondFeedbackQuestionTextArea(this.grow, this.push)
            }
            if (customQuestionLabel != null && customQuestionTextArea != null) {
                // JComponent not working with nullable type.
                // Moreover, the forced type conversion from nullable to normal is regarded as unnecessary
                // and removed during auto-formatting.
                val notNullCustomQuestionLabel: JBLabel = customQuestionLabel as JBLabel
                val notNullCustomQuestionTextArea: EditorTextField = customQuestionTextArea as EditorTextField
                row {
                    notNullCustomQuestionLabel()
                }
                row {
                    notNullCustomQuestionTextArea(this.grow, this.push)
                }
            } else {
                row {
                    thirdFeedbackQuestionLabel()
                }
                row {
                    thirdFeedbackQuestionTextArea(this.grow, this.push)
                }
            }
            row {
                attachFileLabel()
            }
            row {
                attachFile()
            }
            row {
                cell {
                    emailLabel()
                    emailTextField().growPolicy(GrowPolicy.MEDIUM_TEXT)
                }
            }
        }

        return dialogPanel.apply {
            ui = DarculaPanelUI()
            font = UIManager.getFont("Label.font")
            preferredFocusedComponent = firstFeedbackQuestionTextArea
            preferredSize = Dimension(700, 550)
        }
    }

    override fun doValidateAll(): MutableList<ValidationInfo> {
        val validationInfoList = mutableListOf<ValidationInfo>()

        val validationAttachFile = checkAttachFile()
        if (validationAttachFile != null) {
            validationInfoList.add(validationAttachFile)
        }

        if (firstFeedbackQuestionTextArea.text.isEmpty()) {
            validationInfoList.add(
                    ValidationInfo(
                            FeedbackBundle.message("dialog.default.validate.first.feedback.question.empty"),
                            firstFeedbackQuestionTextArea
                    )
            )
        }

        if (emailTextField.text.isNotEmpty() && !isValidEmail(emailTextField.text)) {
            validationInfoList.add(
                    ValidationInfo(
                            FeedbackBundle.message("dialog.default.validate.email"),
                            emailTextField
                    )
            )
        }

        return validationInfoList
    }

    private fun getFormattedFeedback(): String {
        return with(StringBuilder()) {
            //Do not check for emptiness because it is checked during validation
            append("**")
            append(firstFeedbackQuestionLabel.text)
            append("**")
            append("\n")
            append(firstFeedbackQuestionTextArea.text)
            append("\n\n")
            if (secondFeedbackQuestionTextArea.text.isNotEmpty()) {
                append("**")
                append(secondFeedbackQuestionLabel.text)
                append("**")
                append("\n")
                append(secondFeedbackQuestionTextArea.text)
                append("\n\n")
            }
            if (customQuestionLabel != null && customQuestionTextArea != null
                    && customQuestionTextArea!!.text.isNotEmpty()) {
                append("**")
                append(customQuestionLabel!!.text)
                append("**")
                append("\n")
                append(customQuestionTextArea!!.text)
                append("\n\n")
            } else if (thirdFeedbackQuestionTextArea.text.isNotEmpty()) {
                append("**")
                append(thirdFeedbackQuestionLabel.text)
                append("**")
                append("\n")
                append(thirdFeedbackQuestionTextArea.text)
                append("\n\n")
            }
            if (emailTextField.text.isNotEmpty()) {
                append("**")
                append(emailLabel.text)
                append("**")
                append(": ")
                append(emailTextField.text)
            }
            toString()
        }
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

    private fun isValidEmail(text: String): Boolean {
        val emailRegex = Regex(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
        )
        return emailRegex.matches(text)
    }
}