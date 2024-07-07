package com.yugyd.quiz.ui.simplequest.viewmodel

import com.yugyd.quiz.domain.game.api.BaseQuestDomainModel
import com.yugyd.quiz.domain.simplequest.SimpleQuestModel
import com.yugyd.quiz.ui.game.api.GameViewModelDelegate
import com.yugyd.quiz.ui.game.api.GameViewModelDelegate.GameViewModelDelegateArgs
import com.yugyd.quiz.ui.game.api.model.BaseQuestUiModel
import com.yugyd.quiz.ui.game.api.model.HighlightUiModel
import com.yugyd.quiz.ui.simplequest.SimpleQuestUiMapper
import javax.inject.Inject

class SimpleGameViewModelDelegate @Inject constructor(
    private val simpleQuestUiMapper: SimpleQuestUiMapper,
) : GameViewModelDelegate {

    override fun isTypeHandled(domainQuest: BaseQuestDomainModel): Boolean {
        return domainQuest is SimpleQuestModel
    }

    override fun getNewQuestState(
        domainQuest: BaseQuestDomainModel,
        highlight: HighlightUiModel,
        args: GameViewModelDelegateArgs?,
    ): BaseQuestUiModel {
        return simpleQuestUiMapper.map(
            domainQuest as SimpleQuestModel,
            SimpleQuestUiMapper.SimpleArgs(
                answerButtonIsEnabled = (args as EnterCodeGameViewModelDelegateArgs).answerButtonIsEnabled,
                highlight = highlight,
            ),
        )
    }

    override fun getArgs(
        domainQuest: BaseQuestDomainModel,
        userAnswer: String,
        answerButtonIsEnabled: Boolean,
    ): GameViewModelDelegateArgs {
        return EnterCodeGameViewModelDelegateArgs(
            answerButtonIsEnabled = answerButtonIsEnabled
        )
    }

    data class EnterCodeGameViewModelDelegateArgs(
        val answerButtonIsEnabled: Boolean,
    ) : GameViewModelDelegateArgs
}
