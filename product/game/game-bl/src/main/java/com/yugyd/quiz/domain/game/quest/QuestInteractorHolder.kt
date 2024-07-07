package com.yugyd.quiz.domain.game.quest

import com.yugyd.quiz.domain.game.api.BaseQuestDomainModel
import com.yugyd.quiz.domain.game.api.QuestInteractor
import com.yugyd.quiz.domain.game.api.UnknownQuestInteractor
import com.yugyd.quiz.domain.game.api.model.HighlightModel
import com.yugyd.quiz.domain.game.api.model.Quest
import com.yugyd.quiz.domain.game.api.model.QuestType
import javax.inject.Inject

class QuestInteractorHolder @Inject constructor(
    private val questInteractors: Set<@JvmSuppressWildcards QuestInteractor>,
    private val unknownQuestInteractor: UnknownQuestInteractor,
) : QuestInteractor {

    override fun isTypeHandled(type: QuestType) = true

    override fun isQuestHandled(quest: BaseQuestDomainModel) = true

    override fun isTrueAnswer(
        quest: BaseQuestDomainModel,
        index: Int,
        userAnswer: String
    ): Boolean {
        return getQuestInteractor(quest).isTrueAnswer(quest, index, userAnswer)
    }

    override fun getQuestModel(quest: Quest): BaseQuestDomainModel {
        return getQuestInteractor(quest).getQuestModel(quest)
    }

    override fun getHighlightModel(
        quest: BaseQuestDomainModel,
        index: Int,
        isSuccess: Boolean
    ): HighlightModel {
        return getQuestInteractor(quest).getHighlightModel(quest, index, isSuccess)
    }

    private fun getQuestInteractor(quest: Quest): QuestInteractor {
        return questInteractors
            .firstOrNull {
                it.isTypeHandled(quest.type)
            } ?: unknownQuestInteractor
    }

    private fun getQuestInteractor(quest: BaseQuestDomainModel): QuestInteractor {
        return questInteractors
            .firstOrNull {
                it.isQuestHandled(quest)
            } ?: unknownQuestInteractor
    }
}