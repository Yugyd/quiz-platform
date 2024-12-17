package com.yugyd.quiz.ui.enteraiquest

import com.yugyd.quiz.ui.game.api.model.BaseQuestUiModel
import com.yugyd.quiz.ui.game.api.model.QuestUiType
import com.yugyd.quiz.ui.game.api.model.QuestValueUiModel

/**
 * @param questModel the question text
 * @param userAnswer the user's answer, which he entered. Only safe value. Ui state keep in
 * GameViewModel
 * @param isFieldEnabled when checking the question disable editing of the value
 * @param answerState how the answer is highlighted
 * @param answerDescriptionFromAi when you need to display the correct answer from AI
 */
data class EnterAiQuestUiModel(
    override val questModel: QuestValueUiModel,
    val userAnswer: String,
    val isFieldEnabled: Boolean,
    val answerState: AnswerState,
    val answerDescriptionFromAi: String,
    val answerButtonIsEnabled: Boolean,
) : BaseQuestUiModel(
    questModel = questModel,
    type = QuestUiType.ENTER_AI,
) {

    enum class AnswerState {
        SUCCESS,
        FAILED,
        NONE,
    }
}
