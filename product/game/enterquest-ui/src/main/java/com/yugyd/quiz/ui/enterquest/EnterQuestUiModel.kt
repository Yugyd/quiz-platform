package com.yugyd.quiz.ui.enterquest

import com.yugyd.quiz.ui.game.api.model.BaseQuestUiModel
import com.yugyd.quiz.ui.game.api.model.QuestUiType
import com.yugyd.quiz.ui.game.api.model.QuestValueUiModel

data class EnterQuestUiModel(
    override val questModel: QuestValueUiModel,
    val userAnswer: String,
    val isNumberKeyboard: Boolean,
    val isFieldEnabled: Boolean,
    val answerState: AnswerState,
    val trueAnswer: String,
) : BaseQuestUiModel(
    questModel = questModel,
    type = QuestUiType.ENTER,
) {

    enum class AnswerState {
        SUCCESS,
        FAILED,
        NONE,
    }
}
