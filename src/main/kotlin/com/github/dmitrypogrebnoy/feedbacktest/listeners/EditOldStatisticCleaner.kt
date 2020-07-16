package com.github.dmitrypogrebnoy.feedbacktest.listeners

import com.github.dmitrypogrebnoy.feedbacktest.services.EditOldStatisticService
import com.github.dmitrypogrebnoy.feedbacktest.state.editor.EditInfo
import com.intellij.ide.AppLifecycleListener
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import java.time.LocalDate

class EditOldStatisticCleaner : AppLifecycleListener {
    companion object {
        //Time in month
        const val OLD_STAT_TIME: Long = 12
    }

    private val editOldStatisticService: EditOldStatisticService = service()

    override fun appStarting(projectFromCommandLine: Project?) {
        if (editOldStatisticService.state != null) {
            val editorState = editOldStatisticService.state!!.countEditKotlinFile
            val oldTime = LocalDate.now().minusMonths(OLD_STAT_TIME)
            val editorOldState: MutableMap<LocalDate, EditInfo> = mutableMapOf()
            for (entry in editorState.entries) {
                if (!entry.key.isBefore(oldTime)) {
                    editorOldState[entry.key] = entry.value
                }
            }
        }

        super.appStarting(projectFromCommandLine)
    }
}