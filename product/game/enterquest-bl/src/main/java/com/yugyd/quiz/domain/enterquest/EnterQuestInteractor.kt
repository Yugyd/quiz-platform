package com.yugyd.quiz.domain.enterquest

import com.yugyd.quiz.domain.game.api.BaseQuestDomainModel
import com.yugyd.quiz.domain.game.api.QuestInteractor
import com.yugyd.quiz.domain.game.api.TrueAnswerResultModel
import com.yugyd.quiz.domain.game.api.model.HighlightModel
import com.yugyd.quiz.domain.game.api.model.Quest
import com.yugyd.quiz.domain.game.api.model.QuestType
import javax.inject.Inject

class EnterQuestInteractor @Inject constructor() : QuestInteractor {

    override fun isTypeHandled(type: QuestType): Boolean {
        return type == QuestType.ENTER
    }

    override fun isQuestHandled(quest: BaseQuestDomainModel): Boolean {
        return quest is EnterQuestModel
    }

    override suspend fun isTrueAnswer(
        quest: BaseQuestDomainModel,
        selectedUserAnswers: Set<String>,
        enteredUserAnswer: String,
    ): TrueAnswerResultModel {
        val isValid = quest.trueAnswers
            .first()
            .equals(
                enteredUserAnswer.trim(),
                ignoreCase = true,
            )

        return TrueAnswerResultModel(isValid = isValid)
    }

    override fun getQuestModel(quest: Quest): BaseQuestDomainModel {
        return EnterQuestModel(
            id = quest.id,
            quest = quest.quest,
            trueAnswers = setOf(quest.trueAnswer),
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