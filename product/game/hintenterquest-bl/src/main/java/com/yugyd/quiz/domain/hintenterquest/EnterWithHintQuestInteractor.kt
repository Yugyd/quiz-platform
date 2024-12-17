package com.yugyd.quiz.domain.hintenterquest

import com.yugyd.quiz.domain.game.api.BaseQuestDomainModel
import com.yugyd.quiz.domain.game.api.QuestInteractor
import com.yugyd.quiz.domain.game.api.TrueAnswerResultModel
import com.yugyd.quiz.domain.game.api.model.HighlightModel
import com.yugyd.quiz.domain.game.api.model.Quest
import com.yugyd.quiz.domain.game.api.model.QuestType
import javax.inject.Inject

class EnterWithHintQuestInteractor @Inject constructor() : QuestInteractor {

    override fun isTypeHandled(type: QuestType): Boolean {
        return type == QuestType.ENTER_WITH_HINT
    }

    override fun isQuestHandled(quest: BaseQuestDomainModel): Boolean {
        return quest is EnterWithHintQuestModel
    }

    override suspend fun isTrueAnswer(
        quest: BaseQuestDomainModel,
        selectedUserAnswers: Set<String>,
        enteredUserAnswer: String
    ): TrueAnswerResultModel {
        val isValid = quest
            .trueAnswers
            .first()
            .trim()
            .equals(
                enteredUserAnswer.trim(),
                ignoreCase = true,
            )
        return TrueAnswerResultModel(isValid = isValid)
    }

    override fun getQuestModel(quest: Quest): BaseQuestDomainModel {
        return EnterWithHintQuestModel(
            id = quest.id,
            quest = quest.quest,
            trueAnswers = setOf(quest.trueAnswer),
            // TODO Add impl from data layer
            questHint = quest.trueAnswer.toList().sorted().joinToString(separator = ""),
        )
    }

    override fun getHighlightModel(
        quest: BaseQuestDomainModel,
        selectedUserAnswers: Set<String>,
        isSuccess: Boolean,
    ): HighlightModel {
        val highlightModel = if (isSuccess) {
            HighlightModel.State.TRUE
        } else {
            HighlightModel.State.FALSE
        }

        return HighlightModel(state = highlightModel)
    }
}