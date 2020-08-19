package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.user

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.network.UserConditionsConstantsLoader.getMinNumberPreviousEditingKotlinFiles
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.network.UserConditionsConstantsLoader.getMinNumberRelevantEditingKotlinFiles
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.network.UserConditionsConstantsLoader.getNumberDaysForRecentProjects
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.network.UserConditionsConstantsLoader.getNumberDaysPreviousEditingKotlinFiles
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.network.UserConditionsConstantsLoader.getNumberDaysWithoutEditingKotlinFiles
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.network.UserConditionsConstantsLoader.getNumberRecentKotlinProjectsWithoutVcs
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.network.UserConditionsConstantsLoader.getNumberRelevantDaysEditingKotlinFiles
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.editor.EditInfo
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.services.EditingStatisticsService
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

/**
 * Constants and a function to resolve the type of user for the current user.
 */

private const val KOTLIN_PLUGIN_ID = "org.jetbrains.kotlin"

private const val EDU_TOOLS_PLUGIN_ID = "com.jetbrains.edu"

// 100 times
private const val DEFAULT_MIN_NUMBER_RELEVANT_EDITING_KOTLIN_FILES = 100

// 10 days
private const val DEFAULT_NUMBER_RELEVANT_DAYS_EDITING_KOTLIN_FILES = 10

// 7 days
private const val DEFAULT_NUMBER_DAYS_FOR_RECENT_PROJECTS = 7

// 3
private const val DEFAULT_NUMBER_RECENT_KOTLIN_PROJECTS_WITHOUT_VCS = 3

// 7
private const val DEFAULT_NUMBER_DAYS_WITHOUT_EDITING_KOTLIN_FILES = 7

// 14
private const val DEFAULT_NUMBER_DAYS_PREVIOUS_EDITING_KOTLIN_FILES = 14

// 20
private const val DEFAULT_MIN_NUMBER_PREVIOUS_EDITING_KOTLIN_FILES = 20

internal val MIN_NUMBER_RELEVANT_EDITING_KOTLIN_FILES: Int = getMinNumberRelevantEditingKotlinFiles()
        ?: DEFAULT_MIN_NUMBER_RELEVANT_EDITING_KOTLIN_FILES

internal val NUMBER_RELEVANT_DAYS_EDITING_KOTLIN_FILES: Int = getNumberRelevantDaysEditingKotlinFiles()
        ?: DEFAULT_NUMBER_RELEVANT_DAYS_EDITING_KOTLIN_FILES

internal val NUMBER_DAYS_FOR_RECENT_PROJECTS: Int = getNumberDaysForRecentProjects()
        ?: DEFAULT_NUMBER_DAYS_FOR_RECENT_PROJECTS

internal val NUMBER_RECENT_KOTLIN_PROJECTS_WITHOUT_VCS: Int = getNumberRecentKotlinProjectsWithoutVcs()
        ?: DEFAULT_NUMBER_RECENT_KOTLIN_PROJECTS_WITHOUT_VCS

internal val NUMBER_DAYS_WITHOUT_EDITING_KOTLIN_FILES: Int = getNumberDaysWithoutEditingKotlinFiles()
        ?: DEFAULT_NUMBER_DAYS_WITHOUT_EDITING_KOTLIN_FILES

internal val NUMBER_DAYS_PREVIOUS_EDITING_KOTLIN_FILES: Int = getNumberDaysPreviousEditingKotlinFiles()
        ?: DEFAULT_NUMBER_DAYS_PREVIOUS_EDITING_KOTLIN_FILES

internal val MIN_NUMBER_PREVIOUS_EDITING_KOTLIN_FILES: Int = getMinNumberPreviousEditingKotlinFiles()
        ?: DEFAULT_MIN_NUMBER_PREVIOUS_EDITING_KOTLIN_FILES

internal fun checkRelevantNumberKotlinFileEditing(): Boolean {
    val editorState: Map<LocalDate, EditInfo> = service<EditingStatisticsService>().state?.countEditKotlinFile
            ?: return false
    val startRelevantDays = LocalDate.now().minusDays(NUMBER_RELEVANT_DAYS_EDITING_KOTLIN_FILES.toLong())
    val numberRelevantEditKotlin = editorState.entries.fold(0) { acc: Long, entry: Map.Entry<LocalDate, EditInfo> ->
        if (entry.key >= startRelevantDays && entry.key <= LocalDate.now()) {
            acc + entry.value.numberEditing
        } else acc
    }
    return numberRelevantEditKotlin >= MIN_NUMBER_RELEVANT_EDITING_KOTLIN_FILES
}

internal fun checkDaysWithoutEditingKotlinFiles(): Boolean {
    val editorState: Map<LocalDate, EditInfo> = service<EditingStatisticsService>().state?.countEditKotlinFile
            ?: return false
    val startRelevantDays = LocalDate.now().minusDays(NUMBER_DAYS_WITHOUT_EDITING_KOTLIN_FILES.toLong())
    editorState.keys.forEach {
        if (it >= startRelevantDays && it <= LocalDate.now()) {
            return false
        }
    }
    return true
}

internal fun checkPreviousNumberEditingKotlinFiles(): Boolean {
    val editorState: Map<LocalDate, EditInfo> = service<EditingStatisticsService>().state?.countEditKotlinFile
            ?: return false
    val endPreviousDays = LocalDate.now().minusDays(NUMBER_DAYS_WITHOUT_EDITING_KOTLIN_FILES.toLong())
    val startPreviousDays = endPreviousDays.minusDays(NUMBER_DAYS_PREVIOUS_EDITING_KOTLIN_FILES.toLong())
    val numberPreviousEditingKotlinFiles: Long = editorState.entries.fold(0L) { acc: Long, entry ->
        if (entry.key >= startPreviousDays && entry.key <= endPreviousDays) {
            acc + entry.value.numberEditing
        } else acc
    }
    return numberPreviousEditingKotlinFiles >= MIN_NUMBER_PREVIOUS_EDITING_KOTLIN_FILES
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
        it.value.lastOpenDate > LocalDate.now().minusDays(NUMBER_DAYS_FOR_RECENT_PROJECTS.toLong())
                && it.value.hasKotlinFiles && !it.value.hasVcs
    }
    return recentProjectsState.size >= NUMBER_RECENT_KOTLIN_PROJECTS_WITHOUT_VCS
}

internal fun isSendFusEnabled(): Boolean {
    val usageStatisticService: UsageStatisticsPersistenceComponent = service()
    return usageStatisticService.isAllowed
}

internal fun isKotlinProject(project: Project): Boolean {
    return FilenameIndex.getAllFilesByExt(project, "kt").isNotEmpty()
}