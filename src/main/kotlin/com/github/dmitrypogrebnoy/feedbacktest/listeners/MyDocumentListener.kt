package com.github.dmitrypogrebnoy.feedbacktest.listeners

import com.github.dmitrypogrebnoy.feedbacktest.services.StatisticPersistentStateService
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.util.io.FileUtilRt
import com.intellij.openapi.vfs.VirtualFile

class MyDocumentListener : DocumentListener {

    override fun documentChanged(event: DocumentEvent) {
        val fileDocumentManager: FileDocumentManager = FileDocumentManager.getInstance()
        val virtualFile: VirtualFile = fileDocumentManager.getFile(event.document)!!
        val statisticPersistentStateService: StatisticPersistentStateService = service()
        val extensionDocument: String = FileUtilRt.getExtension(virtualFile.name)
        statisticPersistentStateService.state!!.lastEditExtension = extensionDocument
        if (FileUtilRt.extensionEquals(virtualFile.path, "kt") ||
                FileUtilRt.extensionEquals(virtualFile.path, "kts")) {
            statisticPersistentStateService.state!!.editKotlinCount += 1
        }
        super.documentChanged(event)
    }
}