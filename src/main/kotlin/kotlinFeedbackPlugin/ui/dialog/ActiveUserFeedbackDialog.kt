package kotlinFeedbackPlugin.ui.dialog

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.EditorTextField
import com.intellij.ui.components.JBLabel
import kotlinFeedbackPlugin.bundle.FeedbackBundle
import kotlinFeedbackPlugin.ui.notification.SuccessSendFeedbackNotification
import kotlinFeedbackPlugin.ui.notification.generateSuccessSendFeedbackNotification
import kotlinFeedbackPlugin.user.ActiveUserType

/**
 * Feedback dialog for active user type.
 *
 * @see kotlinFeedbackPlugin.user.ActiveUserType
 */

class ActiveUserFeedbackDialog(project: Project) : AttachedFileFeedbackDialog(project) {

    override val titleLabel: JBLabel
    override val sectionLabel: JBLabel
    override val firstFeedbackQuestionLabel: JBLabel
    override val firstFeedbackQuestionTextArea: EditorTextField
    override val secondFeedbackQuestionLabel: JBLabel
    override val secondFeedbackQuestionTextArea: EditorTextField
    override val thirdFeedbackQuestionLabel: JBLabel
    override val thirdFeedbackQuestionTextArea: EditorTextField
    override val feedbackDialogPanel: DialogPanel
    override val customQuestionLabel: JBLabel?
    override val customQuestionTextArea: EditorTextField?
    override val attachFileLabel: JBLabel
    override val attachFile: TextFieldWithBrowseButton
    override val emailLabel: JBLabel
    override val emailTextField: EditorTextField
    override val successSendFeedbackNotification: SuccessSendFeedbackNotification

    init {
        myOKAction = sendFeedbackAction(ActiveUserType)
        setOKButtonText(FeedbackBundle.message("dialog.default.button.ok"))
        titleLabel = createTitleLabel(FeedbackBundle.message("dialog.default.content.title"))
        sectionLabel = createSectionLabel(FeedbackBundle.message("dialog.default.content.section"))
        firstFeedbackQuestionLabel = createFeedbackLabel(FeedbackBundle.message("dialog.default.content.description.first.feedback.question.label"))
        firstFeedbackQuestionTextArea = createFeedbackTextArea(
                project,
                FeedbackBundle.message("dialog.default.content.description.first.feedback.question.textarea.placeholder")
        )
        firstFeedbackQuestionLabel.labelFor = firstFeedbackQuestionTextArea
        secondFeedbackQuestionLabel = createFeedbackLabel(FeedbackBundle.message("dialog.default.content.description.second.feedback.question.label"))
        secondFeedbackQuestionTextArea = createFeedbackTextArea(
                project,
                FeedbackBundle.message("dialog.default.content.description.second.feedback.question.textarea.placeholder")
        )
        secondFeedbackQuestionLabel.labelFor = secondFeedbackQuestionTextArea
        thirdFeedbackQuestionLabel = createFeedbackLabel(FeedbackBundle.message("dialog.default.content.description.third.feedback.question.label"))
        thirdFeedbackQuestionTextArea = createFeedbackTextArea(
                project,
                FeedbackBundle.message("dialog.default.content.description.third.feedback.question.textarea.placeholder")
        )
        thirdFeedbackQuestionLabel.labelFor = thirdFeedbackQuestionTextArea
        customQuestionLabel = createCustomQuestionLabel(ActiveUserType.customQuestion)
        customQuestionTextArea = createCustomQuestionTextField(ActiveUserType.customQuestion)
        attachFileLabel = createAttachFileLabel(FeedbackBundle.message("dialog.default.content.attach.file.label"))
        attachFile = createAttachFileChooser(
                project,
                FeedbackBundle.message("dialog.default.content.attach.file.title"),
                FeedbackBundle.message("dialog.default.content.attach.file.description")
        )
        emailLabel = createEmailLabel()
        emailTextField = createEmailTextField()
        feedbackDialogPanel = createFeedbackDialogPanel()

        successSendFeedbackNotification = generateSuccessSendFeedbackNotification()

        super.init()
        startTrackingValidation()
    }
}