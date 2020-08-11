package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.dialog

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.FeedbackDatesService
import com.intellij.openapi.components.service
import com.intellij.openapi.ui.DialogWrapper
import java.awt.event.ActionEvent
import java.time.LocalDate
import javax.swing.AbstractAction

/**
 * Tracks the cancellation of the feedback dialog and stores the cancellation date in the storage.
 *
 * @see com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.show.FeedbackDatesState
 */

class CancelDialogWindowTracker(private val dialogWrapper: DialogWrapper) : AbstractAction() {

    override fun actionPerformed(e: ActionEvent?) {
        val dateFeedbackState = service<FeedbackDatesService>().state ?: return
        dateFeedbackState.dateCloseFeedbackDialog = LocalDate.now()
        dialogWrapper.close(DialogWrapper.OK_EXIT_CODE)
    }
}