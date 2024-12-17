package com.yugyd.quiz.domain.game.api

import com.yugyd.quiz.domain.game.api.model.HighlightModel
import com.yugyd.quiz.domain.game.api.model.Quest
import com.yugyd.quiz.domain.game.api.model.QuestType

interface QuestInteractor {

    fun isTypeHandled(type: QuestType): Boolean

    fun isQuestHandled(quest: BaseQuestDomainModel): Boolean

    suspend fun isTrueAnswer(
        quest: BaseQuestDomainModel,
        selectedUserAnswers: Set<String>,
        enteredUserAnswer: String,
    ): TrueAnswerResultModel

    fun getQuestModel(quest: Quest): BaseQuestDomainModel

    fun getHighlightModel(
        quest: BaseQuestDomainModel,
        selectedUserAnswers: Set<String>,
        isSuccess: Boolean,
    ): HighlightModel
}
