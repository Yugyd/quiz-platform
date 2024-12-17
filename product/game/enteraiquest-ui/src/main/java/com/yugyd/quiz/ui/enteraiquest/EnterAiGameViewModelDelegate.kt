package com.yugyd.quiz.ui.enteraiquest

import com.yugyd.quiz.domain.enterai.EnterAiQuestModel
import com.yugyd.quiz.domain.game.api.BaseQuestDomainModel
import com.yugyd.quiz.ui.game.api.GameViewModelDelegate
import com.yugyd.quiz.ui.game.api.GameViewModelDelegate.GameViewModelDelegateArgs
import com.yugyd.quiz.ui.game.api.model.BaseQuestUiModel
import com.yugyd.quiz.ui.game.api.model.HighlightUiModel
import com.yugyd.quiz.ui.game.api.model.ProcessAnswerResultModel
import javax.inject.Inject

class EnterAiGameViewModelDelegate @Inject constructor(
    private val enterQuestUiMapper: EnterAiQuestUiMapper,
) : GameViewModelDelegate {

    override fun isTypeHandled(domainQuest: BaseQuestDomainModel): Boolean {
        return domainQuest is EnterAiQuestModel
    }

    override fun getUserAnswers(
        domainQuest: BaseQuestDomainModel,
        quest: BaseQuestUiModel,
    ): Set<String> {
        quest as EnterAiQuestUiModel

        return setOf(quest.userAnswer)
    }

    override fun processUserAnswer(
        domainQuest: BaseQuestDomainModel,
        quest: BaseQuestUiModel,
        userAnswer: String,
        isSelected: Boolean
    ): ProcessAnswerResultModel {
        quest as EnterAiQuestUiModel

        return ProcessAnswerResultModel(
            quest = quest.copy(userAnswer = userAnswer),
            isLastAnswer = false,
        )
    }

    override fun getQuestUiModel(domainQuest: BaseQuestDomainModel): BaseQuestUiModel {
        domainQuest as EnterAiQuestModel

        return enterQuestUiMapper.map(
            model = domainQuest,
            args = EnterAiQuestUiMapper.EnterAiArgs(
                userAnswer = "",
                highlightModel = HighlightUiModel.Default,
                answerButtonIsEnabled = true,
            ),
        )
    }

    override fun updateQuestUiModel(
        domainQuest: BaseQuestDomainModel,
        quest: BaseQuestUiModel,
        highlight: HighlightUiModel,
        args: GameViewModelDelegateArgs?
    ): BaseQuestUiModel {
        quest as EnterAiQuestUiModel
        domainQuest as EnterAiQuestModel
        args as EnterAiGameViewModelDelegateArgs

        return enterQuestUiMapper.map(
            model = domainQuest,
            args = EnterAiQuestUiMapper.EnterAiArgs(
                userAnswer = args.manualAnswer,
                highlightModel = highlight,
                answerButtonIsEnabled = args.answerButtonIsEnabled,
            ),
        )
    }

    override fun getArgs(
        domainQuest: BaseQuestDomainModel,
        userAnswer: String,
        answerButtonIsEnabled: Boolean
    ): GameViewModelDelegateArgs {
        return EnterAiGameViewModelDelegateArgs(
            manualAnswer = userAnswer,
            answerButtonIsEnabled = answerButtonIsEnabled,
        )
    }

    data class EnterAiGameViewModelDelegateArgs(
        val manualAnswer: String,
        val answerButtonIsEnabled: Boolean,
    ) : GameViewModelDelegateArgs
}
