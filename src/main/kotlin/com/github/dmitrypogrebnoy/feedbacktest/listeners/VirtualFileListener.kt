package com.github.dmitrypogrebnoy.feedbacktest.listeners

import com.github.dmitrypogrebnoy.feedbacktest.services.EditorStatisticService
import com.intellij.openapi.components.service
import com.intellij.openapi.util.io.FileUtilRt
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent

class VirtualFileListener : BulkFileListener {

    private val editorStatisticService = service<EditorStatisticService>()

    override fun after(events: MutableList<out VFileEvent>) {
        for (event in events) {
            if (event.file != null) {
                val virtualFile: VirtualFile = event.file!!
                if (FileUtilRt.extensionEquals(virtualFile.path, "kt") ||
                        FileUtilRt.extensionEquals(virtualFile.path, "kts") ||
                        FileUtilRt.extensionEquals(virtualFile.path, "ktm")) {
                    editorStatisticService.state!!.numberEditKotlinFile += 1
                }
            }
        }
        super.after(events)
    }
}