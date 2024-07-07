package com.yugyd.quiz.ui.enterquest

import com.yugyd.quiz.domain.enterquest.EnterQuestModel
import com.yugyd.quiz.domain.game.api.BaseQuestDomainModel
import com.yugyd.quiz.ui.game.api.GameViewModelDelegate
import com.yugyd.quiz.ui.game.api.GameViewModelDelegate.GameViewModelDelegateArgs
import com.yugyd.quiz.ui.game.api.model.BaseQuestUiModel
import com.yugyd.quiz.ui.game.api.model.HighlightUiModel
import javax.inject.Inject

class EnterCodeGameViewModelDelegate @Inject constructor(
    private val enterQuestUiMapper: EnterQuestUiMapper,
) : GameViewModelDelegate {

    override fun isTypeHandled(domainQuest: BaseQuestDomainModel): Boolean {
        return domainQuest is EnterQuestModel
    }

    override fun getNewQuestState(
        domainQuest: BaseQuestDomainModel,
        highlight: HighlightUiModel,
        args: GameViewModelDelegateArgs?,
    ): BaseQuestUiModel {
        return enterQuestUiMapper.map(
            domainQuest as EnterQuestModel,
            EnterQuestUiMapper.EnterCodeArgs(
                userAnswer = (args as EnterCodeGameViewModelDelegateArgs).manualAnswer,
                highlightModel = highlight,
            )
        )
    }

    override fun getArgs(
        domainQuest: BaseQuestDomainModel,
        userAnswer: String,
        answerButtonIsEnabled: Boolean
    ): GameViewModelDelegateArgs? {
        return EnterCodeGameViewModelDelegateArgs(
            manualAnswer = userAnswer,
        )
    }

    data class EnterCodeGameViewModelDelegateArgs(
        val manualAnswer: String,
    ) : GameViewModelDelegateArgs
}
