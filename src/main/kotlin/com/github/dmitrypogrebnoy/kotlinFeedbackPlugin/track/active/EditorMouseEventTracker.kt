package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.track.active

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.active.LastActive.trackActive
import com.intellij.openapi.editor.event.EditorMouseEvent
import com.intellij.openapi.editor.event.EditorMouseListener
import com.intellij.openapi.editor.event.EditorMouseMotionListener

/**
 * Tracks mouse activity in the editor and display the notification after a long inactivity.
 */

class EditorMouseEventTracker : EditorMouseListener, EditorMouseMotionListener {
    override fun mouseClicked(event: EditorMouseEvent) {
        trackActive(event.editor.project ?: return)
    }

    override fun mouseMoved(event: EditorMouseEvent) {
        trackActive(event.editor.project ?: return)
    }
}