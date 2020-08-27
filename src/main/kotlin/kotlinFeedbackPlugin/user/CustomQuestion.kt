package kotlinFeedbackPlugin.user

/**
 * Represents a custom question for a user type.
 */

data class CustomQuestion(
        val question: String,
        val textFieldSettings: QuestionTextFieldSettings
)