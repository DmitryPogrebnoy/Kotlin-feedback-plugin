package kotlinFeedbackPlugin.state.project

import java.time.LocalDate

data class ProjectState(
        val lastOpenDate: LocalDate = LocalDate.now(),
        val hasVcs: Boolean = false,
        val hasKotlinFiles: Boolean = false
)