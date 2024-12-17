package com.yugyd.quiz.ui.game.api

import com.yugyd.quiz.domain.game.api.BaseQuestDomainModel
import com.yugyd.quiz.domain.game.api.exception.QuestTypeException
import com.yugyd.quiz.ui.game.api.GameViewModelDelegate.GameViewModelDelegateArgs
import com.yugyd.quiz.ui.game.api.model.BaseQuestUiModel
import com.yugyd.quiz.ui.game.api.model.HighlightUiModel
import com.yugyd.quiz.ui.game.api.model.ProcessAnswerResultModel
import javax.inject.Inject

class UnknownGameViewModelDelegate @Inject constructor() : GameViewModelDelegate {

    override fun isTypeHandled(domainQuest: BaseQuestDomainModel): Boolean {
        throw QuestTypeException("Unknown quest model")
    }

    override fun getUserAnswers(
        domainQuest: BaseQuestDomainModel,
        quest: BaseQuestUiModel,
    ): Set<String> {
        throw QuestTypeException("Unknown quest model")
    }

    override fun processUserAnswer(
        domainQuest: BaseQuestDomainModel,
        quest: BaseQuestUiModel,
        userAnswer: String,
        isSelected: Boolean
    ): ProcessAnswerResultModel {
        throw QuestTypeException("Unknown quest model")
    }

    override fun getQuestUiModel(domainQuest: BaseQuestDomainModel): BaseQuestUiModel {
        throw QuestTypeException("Unknown quest model")
    }

    override fun updateQuestUiModel(
        domainQuest: BaseQuestDomainModel,
        quest: BaseQuestUiModel,
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
