package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.editor

import com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.editor.converter.EditorStatisticConverter
import com.intellij.util.xmlb.annotations.OptionTag
import java.time.LocalDate

/**
 * Storage of information about user editing Kotlin files.
 *
 * @see com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.editor.EditInfo
 */

data class EditState(
        @OptionTag(converter = EditorStatisticConverter::class)
        var countEditKotlinFile: MutableMap<LocalDate, EditInfo> = mutableMapOf()
)