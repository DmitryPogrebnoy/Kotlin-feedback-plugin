package com.github.dmitrypogrebnoy.feedbacktest.state.editor

import com.github.dmitrypogrebnoy.feedbacktest.state.editor.converter.EditorStatisticConverter
import com.intellij.util.xmlb.annotations.OptionTag
import java.time.LocalDate

data class EditOldState(
        @OptionTag(converter = EditorStatisticConverter::class)
        var countEditKotlinFile: MutableMap<LocalDate, EditInfo> = mutableMapOf()
)