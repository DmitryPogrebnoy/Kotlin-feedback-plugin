package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.setting

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.network.FunctionalitySettingsLoader.getEnableWidgetIconColor
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.network.FunctionalitySettingsLoader.getEnabledNotification
import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.network.FunctionalitySettingsLoader.getEnabledWidget

object FunctionalitySettings {

    private const val DEFAULT_ENABLE_NOTIFICATION = false
    private const val DEFAULT_ENABLE_WIDGET_ICON_COLOR = false
    private const val DEFAULT_ENABLE_WIDGET = true

    val enableNotification: Boolean = getEnabledNotification() ?: DEFAULT_ENABLE_NOTIFICATION
    val enableWidgetIconColor: Boolean = getEnableWidgetIconColor() ?: DEFAULT_ENABLE_WIDGET_ICON_COLOR
    val enableWidget: Boolean = getEnabledWidget() ?: DEFAULT_ENABLE_WIDGET
}