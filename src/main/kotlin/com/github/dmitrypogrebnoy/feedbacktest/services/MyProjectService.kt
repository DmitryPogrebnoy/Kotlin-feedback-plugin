package com.github.dmitrypogrebnoy.feedbacktest.services

import com.intellij.openapi.project.Project
import com.github.dmitrypogrebnoy.feedbacktest.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
