package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.project

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.project.converter.ProjectsStatisticConverter
import com.intellij.util.xmlb.annotations.OptionTag

data class ProjectsStatisticState(
        @OptionTag(converter = ProjectsStatisticConverter::class)
        val projectsStatisticState: MutableMap<String, ProjectState> = mutableMapOf()
)