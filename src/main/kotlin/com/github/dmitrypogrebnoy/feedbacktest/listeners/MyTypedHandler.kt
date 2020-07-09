package com.github.dmitrypogrebnoy.feedbacktest.listeners

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate

class MyTypedHandler : TypedHandlerDelegate() {
    //Process typed event
    /*
    override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): Result {
        val fileDocumentManager: FileDocumentManager = FileDocumentManager.getInstance()
        val virtualFile: VirtualFile = fileDocumentManager.getFile(event.document)!!
        val statisticPersistentStateService: StatisticPersistentStateService = service()
        val extensionDocument: String = FileUtilRt.getExtension(virtualFile.name)
        statisticPersistentStateService.state!!.lastEditExtension = extensionDocument
        if (FileUtilRt.extensionEquals(virtualFile.path,"kt") ||
                FileUtilRt.extensionEquals(virtualFile.path,"kts") ) {
            statisticPersistentStateService.state!!.editKotlinCount += 1
        }

        return super.charTyped(c, project, editor, file)
    }
    */
}