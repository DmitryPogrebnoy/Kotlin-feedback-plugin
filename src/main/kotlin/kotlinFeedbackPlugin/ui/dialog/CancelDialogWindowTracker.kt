package kotlinFeedbackPlugin.ui.dialog

import com.intellij.openapi.components.service
import com.intellij.openapi.ui.DialogWrapper
import kotlinFeedbackPlugin.state.services.FeedbackDatesService
import java.awt.event.ActionEvent
import java.time.LocalDate
import javax.swing.AbstractAction

/**
 * Tracks the cancellation of the feedback dialog and stores the cancellation date in the storage.
 *
 * @see kotlinFeedbackPlugin.state.show.FeedbackDatesState
 */

class CancelDialogWindowTracker(private val dialogWrapper: DialogWrapper) : AbstractAction() {

    override fun actionPerformed(e: ActionEvent?) {
        val dateFeedbackState = service<FeedbackDatesService>().state ?: return
        dateFeedbackState.dateCloseFeedbackDialog = LocalDate.now()
        dialogWrapper.close(DialogWrapper.OK_EXIT_CODE)
    }
}