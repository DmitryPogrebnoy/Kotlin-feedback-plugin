package com.github.dmitrypogrebnoy.feedbacktest.dialog

import com.intellij.ide.ui.laf.darcula.ui.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextArea
import com.intellij.ui.components.JBTextField
import com.intellij.ui.layout.panel
import java.awt.Dimension
import java.awt.Font
import java.awt.Insets
import javax.swing.JComponent
import javax.swing.JFileChooser
import javax.swing.SwingConstants

class FeedbackDialog(project: Project) : DialogWrapper(project) {

    init {
        super.init()
    }

    override fun createCenterPanel(): JComponent? {
        title = "Kotlin Feedback"
        setOKButtonText("Send feedback")
        val font = Font("Verdana", Font.PLAIN, 12)
        //TODO: Set international lang
        val titleLabel: JBLabel = JBLabel("Share Kotlin feedback")
        titleLabel.font = Font("Montserrat", Font.BOLD, 20)

        val sectionLabel: JBLabel = JBLabel(
                "<html>Thank you for sharing your thoughts, comments and ideas with us!" +
                        "<br>We will read and evaluate all feedback carefully!<html>"
        )
        sectionLabel.font = font

        val fromLabel: JBLabel = JBLabel("From:")
        //TODO:Set good font
        fromLabel.font = font.deriveFont(Font.BOLD)
        fromLabel.verticalAlignment = SwingConstants.TOP

        val fromTextFeild: JBTextField = JBTextField()
        fromTextFeild.ui = DarculaTextFieldUI()
        fromTextFeild.font = font
        fromTextFeild.border = DarculaTextBorder()
        fromTextFeild.emptyText.text = "email@example.com"
        fromTextFeild.width

        fromLabel.labelFor = fromTextFeild

        val descriptionLabel: JBLabel = JBLabel("Description:")
        descriptionLabel.ui = DarculaLabelUI()
        descriptionLabel.verticalAlignment = SwingConstants.TOP
        //TODO:Set good font
        descriptionLabel.font = font.deriveFont(Font.BOLD)

        val descriptionTextArea = JBTextArea()
        descriptionTextArea.ui = DarculaTextAreaUI()
        descriptionTextArea.autoscrolls = true
        descriptionTextArea.rows = 7
        descriptionTextArea.preferredSize = Dimension(700, 50)
        descriptionTextArea.border = DarculaTextBorder()
        //TODO: Set margin - too difficult
        // The text area gets out of the label for this reason:
        // https://stackoverflow.com/questions/8792651/how-can-i-add-padding-to-a-jtextfield/8792905
        /*
        descriptionTextArea.border = BorderFactory.createCompoundBorder(
                DarculaTextBorder(),
                descriptionTextArea.border
        )
        */
        descriptionTextArea.lineWrap = true
        descriptionTextArea.wrapStyleWord = true
        descriptionTextArea.emptyText.text = "Please describe your feedback is as match detail as possible"//"Placeholder"
        descriptionTextArea.margin = Insets(5, 5, 5, 5)
        //descriptionTextArea.insets.set(15,15,15,15)
        descriptionTextArea.font = font

        descriptionLabel.labelFor = descriptionTextArea

        val attachLabel: JBLabel = JBLabel("Attach file, screenshots, or logs")
        //TODO:Set good font
        attachLabel.font = font.deriveFont(Font.BOLD)
        attachLabel.ui = DarculaLabelUI()
        attachLabel.verticalAlignment = SwingConstants.TOP

        val attachFileChooser: JFileChooser = JFileChooser()


        val feedbackPanel = panel {
            row {
                titleLabel()
            }
            row {
                sectionLabel()
            }
            row {
                cell(true, true) {
                    fromLabel()
                    fromTextFeild()
                }
                cell(true, true) {
                    descriptionLabel()
                    descriptionTextArea()
                }
                cell(true, true) {
                    attachLabel()

                }
            }
        }

        feedbackPanel.ui = DarculaPanelUI()
        feedbackPanel.font = font
        feedbackPanel.preferredFocusedComponent = fromTextFeild

        return feedbackPanel
    }
}