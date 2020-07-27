package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.track.edit

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.editor.EditInfo
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.EditStatisticService
import com.intellij.openapi.components.service
import com.intellij.openapi.util.io.FileUtilRt
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import java.time.LocalDate

class EditFileListener : BulkFileListener {

    private val editRelevantStatisticService = service<EditStatisticService>()

    override fun after(events: MutableList<out VFileEvent>) {
        for (event in events) {
            if (event.file != null) {
                val virtualFile: VirtualFile = event.file!!
                if (FileUtilRt.extensionEquals(virtualFile.path, "kt")) {
                    val localDate = LocalDate.now()
                    if (editRelevantStatisticService.state!!.countEditKotlinFile[localDate] != null) {
                        editRelevantStatisticService.state!!.countEditKotlinFile[localDate] =
                                EditInfo(
                                        editRelevantStatisticService.state!!.countEditKotlinFile[localDate]!!.numberEditing + 1
                                )
                    } else {
                        editRelevantStatisticService.state!!.countEditKotlinFile[localDate] =
                                EditInfo(
                                        1
                                )
                    }

                }
            }
        }
        super.after(events)
    }
}