package com.yugyd.quiz.ui.game.api

import com.yugyd.quiz.domain.game.api.BaseQuestDomainModel
import com.yugyd.quiz.ui.game.api.model.BaseQuestUiModel
import com.yugyd.quiz.ui.game.api.model.HighlightUiModel

interface GameViewModelDelegate {

    fun isTypeHandled(domainQuest: BaseQuestDomainModel): Boolean

    fun getNewQuestState(
        domainQuest: BaseQuestDomainModel,
        highlight: HighlightUiModel,
        args: GameViewModelDelegateArgs?,
    ): BaseQuestUiModel

    fun getArgs(
        domainQuest: BaseQuestDomainModel,
        userAnswer: String,
        answerButtonIsEnabled: Boolean,
    ): GameViewModelDelegateArgs?

    interface GameViewModelDelegateArgs
}
