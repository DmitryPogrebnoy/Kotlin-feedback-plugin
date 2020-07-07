package com.github.dmitrypogrebnoy.feedbacktest.dialog

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import javax.swing.JComponent
import javax.swing.JPanel

class FeedbackDialog(project: Project) : DialogWrapper(project) {

    init {
        super.init()
    }

    override fun createCenterPanel(): JComponent? {
        //Panel stub
        val panel = JPanel()
        return panel
    }
}