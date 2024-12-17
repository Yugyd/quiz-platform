package com.yugyd.quiz.gameui.game

import com.yugyd.quiz.domain.game.api.BaseQuestDomainModel
import com.yugyd.quiz.ui.game.api.GameViewModelDelegate
import com.yugyd.quiz.ui.game.api.GameViewModelDelegate.GameViewModelDelegateArgs
import com.yugyd.quiz.ui.game.api.UnknownGameViewModelDelegate
import com.yugyd.quiz.ui.game.api.model.BaseQuestUiModel
import com.yugyd.quiz.ui.game.api.model.HighlightUiModel
import com.yugyd.quiz.ui.game.api.model.ProcessAnswerResultModel
import javax.inject.Inject

class GameViewModelDelegateHolder @Inject constructor(
    private val gameViewModelDelegates: Set<@JvmSuppressWildcards GameViewModelDelegate>,
    private val unknownGameViewModelDelegate: UnknownGameViewModelDelegate,
) : GameViewModelDelegate {

    override fun isTypeHandled(domainQuest: BaseQuestDomainModel): Boolean {
        return getDelegate(domainQuest).isTypeHandled(domainQuest)
    }

    override fun getUserAnswers(
        domainQuest: BaseQuestDomainModel,
        quest: BaseQuestUiModel,
    ): Set<String> {
        return getDelegate(domainQuest).getUserAnswers(domainQuest, quest)
    }

    override fun processUserAnswer(
        domainQuest: BaseQuestDomainModel,
        quest: BaseQuestUiModel,
        userAnswer: String,
        isSelected: Boolean
    ): ProcessAnswerResultModel {
        return getDelegate(domainQuest).processUserAnswer(
            domainQuest = domainQuest,
            quest = quest,
            userAnswer = userAnswer,
            isSelected = isSelected,
        )
    }

    override fun getQuestUiModel(domainQuest: BaseQuestDomainModel): BaseQuestUiModel {
        return getDelegate(domainQuest).getQuestUiModel(domainQuest = domainQuest)
    }

    override fun updateQuestUiModel(
        domainQuest: BaseQuestDomainModel,
        quest: BaseQuestUiModel,
        highlight: HighlightUiModel,
        args: GameViewModelDelegateArgs?
    ): BaseQuestUiModel {
        return getDelegate(domainQuest).updateQuestUiModel(
            domainQuest = domainQuest,
            quest = quest,
            highlight = highlight,
            args = args,
        )
    }

    override fun getArgs(
        domainQuest: BaseQuestDomainModel,
        userAnswer: String,
        answerButtonIsEnabled: Boolean
    ): GameViewModelDelegateArgs? {
        return getDelegate(domainQuest).getArgs(
            domainQuest = domainQuest,
            userAnswer = userAnswer,
            answerButtonIsEnabled = answerButtonIsEnabled,
        )
    }

    private fun getDelegate(domainQuest: BaseQuestDomainModel): GameViewModelDelegate {
        return gameViewModelDelegates
            .firstOrNull {
                it.isTypeHandled(domainQuest)
            } ?: unknownGameViewModelDelegate
    }
}
