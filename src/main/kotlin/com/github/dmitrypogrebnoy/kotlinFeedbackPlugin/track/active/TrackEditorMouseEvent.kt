package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.track.active

import com.intellij.openapi.editor.event.EditorMouseEvent
import com.intellij.openapi.editor.event.EditorMouseListener
import com.intellij.openapi.editor.event.EditorMouseMotionListener

class TrackEditorMouseEvent : EditorMouseListener, EditorMouseMotionListener {
    override fun mouseClicked(event: EditorMouseEvent) {
        trackActive()
    }

    override fun mouseMoved(e: EditorMouseEvent) {
        trackActive()
    }
}