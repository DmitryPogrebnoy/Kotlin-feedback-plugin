package com.github.dmitrypogrebnoy.feedbacktest

import com.github.dmitrypogrebnoy.feedbacktest.listeners.MyDocumentListener
import com.intellij.ide.AppLifecycleListener
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer

class Registrator : AppLifecycleListener {

    override fun appStarting(projectFromCommandLine: Project?) {
        //Add listeners
        EditorFactory.getInstance().eventMulticaster.addDocumentListener(MyDocumentListener(), Disposer.newDisposable())

        super.appStarting(projectFromCommandLine)
    }
}