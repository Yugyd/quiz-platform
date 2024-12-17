package com.yugyd.quiz.ui.simplequest.viewmodel

import com.yugyd.quiz.domain.game.api.BaseQuestDomainModel
import com.yugyd.quiz.domain.simplequest.SimpleQuestModel
import com.yugyd.quiz.ui.game.api.GameViewModelDelegate
import com.yugyd.quiz.ui.game.api.GameViewModelDelegate.GameViewModelDelegateArgs
import com.yugyd.quiz.ui.game.api.model.BaseQuestUiModel
import com.yugyd.quiz.ui.game.api.model.HighlightUiModel
import com.yugyd.quiz.ui.game.api.model.ProcessAnswerResultModel
import com.yugyd.quiz.ui.simplequest.SimpleQuestUiMapper
import com.yugyd.quiz.ui.simplequest.SimpleQuestUiModel
import javax.inject.Inject

class SimpleGameViewModelDelegate @Inject constructor(
    private val simpleQuestUiMapper: SimpleQuestUiMapper,
) : GameViewModelDelegate {

    override fun isTypeHandled(domainQuest: BaseQuestDomainModel): Boolean {
        return domainQuest is SimpleQuestModel
    }

    override fun processUserAnswer(
        domainQuest: BaseQuestDomainModel,
        quest: BaseQuestUiModel,
        userAnswer: String,
        isSelected: Boolean,
    ): ProcessAnswerResultModel {
        quest as SimpleQuestUiModel

        return ProcessAnswerResultModel(
            quest = quest.copy(selectedAnswer = userAnswer),
            isLastAnswer = false,
        )
    }

    override fun getUserAnswers(
        domainQuest: BaseQuestDomainModel,
        quest: BaseQuestUiModel,
    ): Set<String> {
        quest as SimpleQuestUiModel

        return setOf(quest.selectedAnswer).filterNotNull().toSet()
    }

    override fun getQuestUiModel(domainQuest: BaseQuestDomainModel): BaseQuestUiModel {
        domainQuest as SimpleQuestModel

        val mapperArgs = SimpleQuestUiMapper.SimpleArgs(
            answerButtonIsEnabled = true,
            highlight = HighlightUiModel.Default,
        )
        return simpleQuestUiMapper.map(
            model = domainQuest,
            args = mapperArgs,
        )
    }

    override fun updateQuestUiModel(
        domainQuest: BaseQuestDomainModel,
        quest: BaseQuestUiModel,
        highlight: HighlightUiModel,
        args: GameViewModelDelegateArgs?
    ): BaseQuestUiModel {
        quest as SimpleQuestUiModel

        return quest.copy(
            highlight = highlight,
            answerButtonIsEnabled = (args as SimpleGameViewModelDelegateArgs).answerButtonIsEnabled,
        )
    }

    override fun getArgs(
        domainQuest: BaseQuestDomainModel,
        userAnswer: String,
        answerButtonIsEnabled: Boolean,
    ): GameViewModelDelegateArgs {
        return SimpleGameViewModelDelegateArgs(answerButtonIsEnabled = answerButtonIsEnabled)
    }

    data class SimpleGameViewModelDelegateArgs(
        val answerButtonIsEnabled: Boolean,
    ) : GameViewModelDelegateArgs
}
