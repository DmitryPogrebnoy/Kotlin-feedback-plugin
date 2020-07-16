package com.github.dmitrypogrebnoy.feedbacktest.listeners

import com.github.dmitrypogrebnoy.feedbacktest.services.EditRelevantStatisticService
import com.github.dmitrypogrebnoy.feedbacktest.state.editor.EditInfo
import com.intellij.openapi.components.service
import com.intellij.openapi.util.io.FileUtilRt
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import java.time.LocalDate

class VirtualFileListener : BulkFileListener {

    private val editRelevantStatisticService = service<EditRelevantStatisticService>()

    override fun after(events: MutableList<out VFileEvent>) {
        for (event in events) {
            if (event.file != null) {
                val virtualFile: VirtualFile = event.file!!
                if (FileUtilRt.extensionEquals(virtualFile.path, "kt") ||
                        FileUtilRt.extensionEquals(virtualFile.path, "kts") ||
                        FileUtilRt.extensionEquals(virtualFile.path, "ktm")) {
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