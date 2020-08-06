package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.dialog

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.DateFeedbackStatService
import com.intellij.openapi.components.service
import com.intellij.openapi.ui.DialogWrapper
import java.awt.event.ActionEvent
import java.time.LocalDate
import javax.swing.AbstractAction

class CancelDialogWindowTracker(private val dialogWrapper: DialogWrapper) : AbstractAction() {

    override fun actionPerformed(e: ActionEvent?) {
        val dateFeedbackState = service<DateFeedbackStatService>().state ?: return
        dateFeedbackState.dateCloseFeedbackDialog = LocalDate.now()
        dialogWrapper.close(DialogWrapper.OK_EXIT_CODE)
    }
}