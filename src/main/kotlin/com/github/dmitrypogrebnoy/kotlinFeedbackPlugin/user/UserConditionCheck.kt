package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.editor.EditInfo
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.EditStatisticService
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.ProjectsStatisticService
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.internal.statistic.persistence.UsageStatisticsPersistenceComponent
import com.intellij.openapi.application.ex.ApplicationInfoEx
import com.intellij.openapi.components.service
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.ProjectLevelVcsManager
import com.intellij.psi.search.FilenameIndex
import java.time.LocalDate

internal const val KOTLIN_PLUGIN_ID = "org.jetbrains.kotlin"

internal const val EDU_TOOLS_PLUGIN_ID = "com.jetbrains.edu"

// 5 times
internal const val MIN_NUMBER_RELEVANT_EDITING_KOTLIN_FILE = 0

// 10 days
internal const val RELEVANT_DAYS: Long = 10

// 7 days
internal const val RECENT_PROJECT_DAYS: Long = 7

internal const val RECENT_KOTLIN_PROJECT_WITHOUT_VCS = 3

internal fun checkRelevantNumberKotlinFileEditing(): Boolean {
    val editorState: Map<LocalDate, EditInfo> = service<EditStatisticService>().state?.countEditKotlinFile
            ?: return false
    val startRelevantDays = LocalDate.now().minusDays(RELEVANT_DAYS)
    val numberRelevantEditKotlin = editorState.entries.fold(0) { acc: Long, entry: Map.Entry<LocalDate, EditInfo> ->
        if (entry.key >= startRelevantDays && entry.key <= LocalDate.now()) {
            acc + entry.value.numberEditing
        } else acc
    }
    return numberRelevantEditKotlin > MIN_NUMBER_RELEVANT_EDITING_KOTLIN_FILE
}

internal fun needCollectUserFeedback(): Boolean {
    return isKotlinPluginInstalled() && isSendFusEnabled()
}

internal fun isKotlinPluginInstalled(): Boolean {
    return PluginManagerCore.getPlugin(PluginId.findId(KOTLIN_PLUGIN_ID)) != null
}

internal fun isKotlinPluginEnabled(): Boolean {
    val kotlinPluginDescriptor: IdeaPluginDescriptor = PluginManagerCore.getPlugin(
            PluginId.findId(KOTLIN_PLUGIN_ID)
    ) ?: return false
    return kotlinPluginDescriptor.isEnabled
}

internal fun isKotlinPluginEAP(): Boolean {
    val kotlinPluginDescriptor: IdeaPluginDescriptor = PluginManagerCore.getPlugin(
            PluginId.findId(KOTLIN_PLUGIN_ID)
    ) ?: return false
    return kotlinPluginDescriptor.version.contains("M")
            || kotlinPluginDescriptor.version.contains("eap")
}

internal fun isEduToolsPluginInstalled(): Boolean {
    return PluginManagerCore.getPlugin(PluginId.findId(EDU_TOOLS_PLUGIN_ID)) != null
}

internal fun isIntellijIdeaEAP(): Boolean {
    return ApplicationInfoEx.getInstanceEx().isEAP
}

internal fun isEduToolsPluginEnabled(): Boolean {
    val eduToolsPluginDescriptor: IdeaPluginDescriptor = PluginManagerCore.getPlugin(
            PluginId.findId(EDU_TOOLS_PLUGIN_ID)
    ) ?: return false
    return eduToolsPluginDescriptor.isEnabled
}

internal fun hasVcs(project: Project): Boolean {
    return ProjectLevelVcsManager.getInstance(project).hasActiveVcss()
}

internal fun openedManyRecentProjectsWithoutVcs(): Boolean {
    val projectsStatisticService: ProjectsStatisticService = service()
    val state = projectsStatisticService.state ?: return false
    val recentProjectsState = state.projectsStatisticState.filter {
        it.value.lastOpenDate > LocalDate.now().minusDays(RECENT_PROJECT_DAYS)
                && it.value.hasKotlinFiles && !it.value.hasVcs
    }
    return recentProjectsState.size >= RECENT_KOTLIN_PROJECT_WITHOUT_VCS
}

internal fun isSendFusEnabled(): Boolean {
    val usageStatisticService: UsageStatisticsPersistenceComponent = service()
    return usageStatisticService.isAllowed
}

internal fun isKotlinProject(project: Project): Boolean {
    return FilenameIndex.getAllFilesByExt(project, "kt").isNotEmpty()
}