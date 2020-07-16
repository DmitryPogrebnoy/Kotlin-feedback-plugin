package com.github.dmitrypogrebnoy.feedbacktest.listeners

import com.github.dmitrypogrebnoy.feedbacktest.services.EditOldStatisticService
import com.github.dmitrypogrebnoy.feedbacktest.services.EditRelevantStatisticService
import com.github.dmitrypogrebnoy.feedbacktest.state.editor.EditInfo
import com.intellij.ide.AppLifecycleListener
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import java.time.LocalDate

class EditRelevantStatisticCleaner : AppLifecycleListener {

    companion object {
        // Relevant time in days
        const val RELEVANT_TIME_STAT: Long = 14
    }

    private val editRelevantStatisticService: EditRelevantStatisticService = service()
    private val editOldStatisticService: EditOldStatisticService = service()

    override fun appStarting(projectFromCommandLine: Project?) {
        if (editRelevantStatisticService.state != null) {
            val editorState = editRelevantStatisticService.state!!.countEditKotlinFile
            val relevantTime = LocalDate.now().minusDays(RELEVANT_TIME_STAT)
            val editorRelevantState: MutableMap<LocalDate, EditInfo> = mutableMapOf()
            for (entry in editorState.entries) {
                if (!entry.key.isBefore(relevantTime)) {
                    editorRelevantState[entry.key] = entry.value
                } else {
                    editOldStatisticService.state!!.countEditKotlinFile[entry.key] = entry.value
                }
            }
        }

        super.appStarting(projectFromCommandLine)
    }
}