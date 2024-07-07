package com.yugyd.quiz.gameui.game

import com.yugyd.quiz.domain.game.api.BaseQuestDomainModel
import com.yugyd.quiz.ui.game.api.GameViewModelDelegate
import com.yugyd.quiz.ui.game.api.GameViewModelDelegate.GameViewModelDelegateArgs
import com.yugyd.quiz.ui.game.api.UnknownGameViewModelDelegate
import com.yugyd.quiz.ui.game.api.model.BaseQuestUiModel
import com.yugyd.quiz.ui.game.api.model.HighlightUiModel
import javax.inject.Inject

class GameViewModelDelegateHolder @Inject constructor(
    private val gameViewModelDelegates: Set<@JvmSuppressWildcards GameViewModelDelegate>,
    private val unknownGameViewModelDelegate: UnknownGameViewModelDelegate,
) : GameViewModelDelegate {

    override fun isTypeHandled(domainQuest: BaseQuestDomainModel): Boolean {
        return getDelegate(domainQuest).isTypeHandled(domainQuest)
    }

    override fun getNewQuestState(
        domainQuest: BaseQuestDomainModel,
        highlight: HighlightUiModel,
        args: GameViewModelDelegateArgs?
    ): BaseQuestUiModel {
        return getDelegate(domainQuest).getNewQuestState(
            domainQuest = domainQuest,
            highlight = highlight,
            args = args,
        )
    }

    override fun getArgs(
        domainQuest: BaseQuestDomainModel,
        userAnswer: String,
        answerButtonIsEnabled: Boolean
    ): GameViewModelDelegateArgs? {
        return getDelegate(domainQuest).getArgs(domainQuest, userAnswer, answerButtonIsEnabled)
    }

    private fun getDelegate(domainQuest: BaseQuestDomainModel): GameViewModelDelegate {
        return gameViewModelDelegates
            .firstOrNull {
                it.isTypeHandled(domainQuest)
            } ?: unknownGameViewModelDelegate
    }
}
