package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.track.active

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

class TrackEditorTypingEvent : TypedHandlerDelegate() {
    override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): Result {
        trackActive()
        return Result.CONTINUE
    }
}