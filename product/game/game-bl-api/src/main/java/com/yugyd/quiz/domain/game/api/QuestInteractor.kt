package com.yugyd.quiz.domain.game.api

import com.yugyd.quiz.domain.game.api.model.HighlightModel
import com.yugyd.quiz.domain.game.api.model.Quest
import com.yugyd.quiz.domain.game.api.model.QuestType

interface QuestInteractor {

    fun isTypeHandled(type: QuestType): Boolean

    fun isQuestHandled(quest: BaseQuestDomainModel): Boolean

    fun isTrueAnswer(
        quest: BaseQuestDomainModel,
        index: Int,
        userAnswer: String
    ): Boolean

    fun getQuestModel(quest: Quest): BaseQuestDomainModel

    fun getHighlightModel(
        quest: BaseQuestDomainModel,
        index: Int,
        isSuccess: Boolean,
    ): HighlightModel
}
