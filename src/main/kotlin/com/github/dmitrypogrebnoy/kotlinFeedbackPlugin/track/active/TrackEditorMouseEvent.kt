package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.track.active

import com.intellij.openapi.editor.event.EditorMouseEvent
import com.intellij.openapi.editor.event.EditorMouseListener
import com.intellij.openapi.editor.event.EditorMouseMotionListener

class TrackEditorMouseEvent : EditorMouseListener, EditorMouseMotionListener {
    override fun mouseClicked(event: EditorMouseEvent) {
        trackActive(event.editor.project ?: return)
    }

    override fun mouseMoved(event: EditorMouseEvent) {
        trackActive(event.editor.project ?: return)
    }
}