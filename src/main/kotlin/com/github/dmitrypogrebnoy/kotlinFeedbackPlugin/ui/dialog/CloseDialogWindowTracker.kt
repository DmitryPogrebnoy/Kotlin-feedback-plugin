package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.ui.dialog

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.DateFeedbackStatService
import com.intellij.openapi.components.service
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import java.time.LocalDate

class CloseDialogWindowTracker : WindowListener {
    override fun windowDeiconified(e: WindowEvent?) {
    }

    override fun windowClosing(e: WindowEvent?) {
        val dateFeedbackState = service<DateFeedbackStatService>().state ?: return
        dateFeedbackState.dateCloseFeedbackDialog = LocalDate.now()
    }

    override fun windowClosed(e: WindowEvent?) {
    }

    override fun windowActivated(e: WindowEvent?) {
    }

    override fun windowDeactivated(e: WindowEvent?) {
    }

    override fun windowOpened(e: WindowEvent?) {
    }

    override fun windowIconified(e: WindowEvent?) {
    }
}