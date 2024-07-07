package com.yugyd.quiz.ui.enterquest

import com.yugyd.quiz.ui.game.api.model.BaseQuestUiModel

data class EnterQuestUiModel(
    override val quest: String,
    val userAnswer: String,
    val isNumberKeyboard: Boolean,
    val isFieldEnabled: Boolean,
    val isButtonEnabled: Boolean,
    val answerState: AnswerState,
    val trueAnswer: String,
) : BaseQuestUiModel {

    enum class AnswerState {
        SUCCESS,
        FAILED,
        NONE,
    }
}
