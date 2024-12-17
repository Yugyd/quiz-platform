package com.yugyd.quiz.ui.hintenterquest

import com.yugyd.quiz.domain.game.api.BaseQuestDomainModel
import com.yugyd.quiz.domain.hintenterquest.EnterWithHintQuestModel
import com.yugyd.quiz.ui.game.api.GameViewModelDelegate
import com.yugyd.quiz.ui.game.api.GameViewModelDelegate.GameViewModelDelegateArgs
import com.yugyd.quiz.ui.game.api.model.BaseQuestUiModel
import com.yugyd.quiz.ui.game.api.model.HighlightUiModel
import com.yugyd.quiz.ui.game.api.model.ProcessAnswerResultModel
import javax.inject.Inject

class EnterWithHintGameViewModelDelegate @Inject constructor(
    private val enterQuestUiMapper: EnterWithHintQuestUiMapper,
) : GameViewModelDelegate {

    override fun isTypeHandled(domainQuest: BaseQuestDomainModel): Boolean {
        return domainQuest is EnterWithHintQuestModel
    }

    override fun getUserAnswers(
        domainQuest: BaseQuestDomainModel,
        quest: BaseQuestUiModel,
    ): Set<String> {
        quest as EnterWithHintQuestUiModel

        return setOf(quest.userAnswer)
    }

    override fun processUserAnswer(
        domainQuest: BaseQuestDomainModel,
        quest: BaseQuestUiModel,
        userAnswer: String,
        isSelected: Boolean
    ): ProcessAnswerResultModel {
        quest as EnterWithHintQuestUiModel

        return ProcessAnswerResultModel(
            quest = quest.copy(userAnswer = userAnswer),
            isLastAnswer = false,
        )
    }

    override fun getQuestUiModel(domainQuest: BaseQuestDomainModel): BaseQuestUiModel {
        domainQuest as EnterWithHintQuestModel

        return enterQuestUiMapper.map(
            model = domainQuest,
            args = EnterWithHintQuestUiMapper.HintEnterArgs(
                userAnswer = "",
                highlightModel = HighlightUiModel.Default,
            ),
        )
    }

    override fun updateQuestUiModel(
        domainQuest: BaseQuestDomainModel,
        quest: BaseQuestUiModel,
        highlight: HighlightUiModel,
        args: GameViewModelDelegateArgs?
    ): BaseQuestUiModel {
        quest as EnterWithHintQuestUiModel
        domainQuest as EnterWithHintQuestModel
        args as EnterWithHintGameViewModelDelegateArgs

        return enterQuestUiMapper.map(
            model = domainQuest,
            args = EnterWithHintQuestUiMapper.HintEnterArgs(
                userAnswer = args.manualAnswer,
                highlightModel = highlight,
            ),
        )
    }

    override fun getArgs(
        domainQuest: BaseQuestDomainModel,
        userAnswer: String,
        answerButtonIsEnabled: Boolean
    ): GameViewModelDelegateArgs {
        return EnterWithHintGameViewModelDelegateArgs(manualAnswer = userAnswer)
    }

    data class EnterWithHintGameViewModelDelegateArgs(
        val manualAnswer: String,
    ) : GameViewModelDelegateArgs
}
