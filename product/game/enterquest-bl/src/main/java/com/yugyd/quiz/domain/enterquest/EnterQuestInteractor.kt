package com.yugyd.quiz.domain.enterquest

import com.yugyd.quiz.domain.game.api.BaseQuestDomainModel
import com.yugyd.quiz.domain.game.api.QuestInteractor
import com.yugyd.quiz.domain.game.api.model.HighlightModel
import com.yugyd.quiz.domain.game.api.model.Quest
import com.yugyd.quiz.domain.game.api.model.QuestType
import javax.inject.Inject

class EnterQuestInteractor @Inject constructor() : QuestInteractor {

    override fun isTypeHandled(type: QuestType): Boolean {
        return type == QuestType.ENTER_CODE
    }

    override fun isQuestHandled(quest: BaseQuestDomainModel): Boolean {
        return quest is EnterQuestModel
    }

    override fun isTrueAnswer(
        quest: BaseQuestDomainModel,
        index: Int,
        userAnswer: String
    ): Boolean {
        return quest.trueAnswer
            .trim()
            .equals(
                userAnswer.trim(),
                ignoreCase = true,
            )
    }

    override fun getQuestModel(quest: Quest): BaseQuestDomainModel {
        return EnterQuestModel(
            id = quest.id,
            quest = quest.quest,
            trueAnswer = quest.trueAnswer,
        )
    }

    override fun getHighlightModel(
        quest: BaseQuestDomainModel,
        index: Int,
        isSuccess: Boolean
    ): HighlightModel {
        val highlightModel = if (isSuccess) {
            HighlightModel.State.TRUE
        } else {
            HighlightModel.State.FALSE
        }

        return HighlightModel(state = highlightModel)
    }
}