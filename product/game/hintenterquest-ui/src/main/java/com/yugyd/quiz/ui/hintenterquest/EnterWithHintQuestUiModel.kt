package com.yugyd.quiz.ui.hintenterquest

import com.yugyd.quiz.ui.game.api.model.BaseQuestUiModel
import com.yugyd.quiz.ui.game.api.model.QuestUiType
import com.yugyd.quiz.ui.game.api.model.QuestValueUiModel

data class EnterWithHintQuestUiModel(
    override val questModel: QuestValueUiModel,
    val answerHint: String,
    val userAnswer: String,
    val isNumberKeyboard: Boolean,
    val isFieldEnabled: Boolean,
    val answerState: AnswerState,
    val trueAnswer: String,
) : BaseQuestUiModel(
    questModel = questModel,
    type = QuestUiType.ENTER_WITH_HINT,
) {

    enum class AnswerState {
        SUCCESS,
        FAILED,
        NONE,
    }
}
