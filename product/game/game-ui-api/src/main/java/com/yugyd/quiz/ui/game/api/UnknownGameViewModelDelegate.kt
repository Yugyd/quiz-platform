package com.yugyd.quiz.ui.game.api

import com.yugyd.quiz.domain.game.api.BaseQuestDomainModel
import com.yugyd.quiz.domain.game.api.exception.QuestTypeException
import com.yugyd.quiz.ui.game.api.GameViewModelDelegate.GameViewModelDelegateArgs
import com.yugyd.quiz.ui.game.api.model.BaseQuestUiModel
import com.yugyd.quiz.ui.game.api.model.HighlightUiModel
import javax.inject.Inject

class UnknownGameViewModelDelegate @Inject constructor() : GameViewModelDelegate {

    override fun isTypeHandled(domainQuest: BaseQuestDomainModel): Boolean {
        throw QuestTypeException("Unknown quest model")
    }

    override fun getNewQuestState(
        domainQuest: BaseQuestDomainModel,
        highlight: HighlightUiModel,
        args: GameViewModelDelegateArgs?
    ): BaseQuestUiModel {
        throw QuestTypeException("Unknown quest model")
    }

    override fun getArgs(
        domainQuest: BaseQuestDomainModel,
        userAnswer: String,
        answerButtonIsEnabled: Boolean
    ) = throw QuestTypeException("Unknown quest model")
}
