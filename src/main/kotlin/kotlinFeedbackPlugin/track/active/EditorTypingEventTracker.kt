package kotlinFeedbackPlugin.track.active

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import kotlinFeedbackPlugin.state.active.LastActive.trackActive

/**
 * Tracks keyboard activity in the editor and display the notification after a long inactivity.
 */

class EditorTypingEventTracker : TypedHandlerDelegate() {
    override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): Result {
        trackActive(project)
        return Result.CONTINUE
    }
}