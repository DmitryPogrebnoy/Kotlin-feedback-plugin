package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.state.editor

data class EditInfo(val numberEditing: Long = DEFAULT_NUMBER_EDIT_KOTLIN_FILE) {
    companion object {
        private const val DEFAULT_NUMBER_EDIT_KOTLIN_FILE: Long = 0
    }
}