package kotlinFeedbackPlugin.state.editor

import com.intellij.util.xmlb.annotations.OptionTag
import kotlinFeedbackPlugin.state.editor.converter.EditorStatisticConverter
import java.time.LocalDate

/**
 * Storage of information about user editing Kotlin files.
 *
 * @see kotlinFeedbackPlugin.state.editor.EditInfo
 */

data class EditState(
        @OptionTag(converter = EditorStatisticConverter::class)
        var countEditKotlinFile: MutableMap<LocalDate, EditInfo> = mutableMapOf()
)