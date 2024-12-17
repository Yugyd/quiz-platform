package com.yugyd.quiz.ui.selectboolquest.viewmodel

import com.yugyd.quiz.domain.game.api.BaseQuestDomainModel
import com.yugyd.quiz.domain.selectboolquest.SelectBoolQuestModel
import com.yugyd.quiz.ui.game.api.GameViewModelDelegate
import com.yugyd.quiz.ui.game.api.GameViewModelDelegate.GameViewModelDelegateArgs
import com.yugyd.quiz.ui.game.api.model.BaseQuestUiModel
import com.yugyd.quiz.ui.game.api.model.HighlightUiModel
import com.yugyd.quiz.ui.game.api.model.ProcessAnswerResultModel
import com.yugyd.quiz.ui.selectboolquest.SelectBoolQuestUiMapper
import com.yugyd.quiz.ui.selectboolquest.SelectBoolQuestUiModel
import javax.inject.Inject

class SelectBoolGameViewModelDelegate @Inject constructor(
    private val simpleQuestUiMapper: SelectBoolQuestUiMapper,
) : GameViewModelDelegate {

    override fun isTypeHandled(domainQuest: BaseQuestDomainModel): Boolean {
        return domainQuest is SelectBoolQuestModel
    }

    override fun processUserAnswer(
        domainQuest: BaseQuestDomainModel,
        quest: BaseQuestUiModel,
        userAnswer: String,
        isSelected: Boolean,
    ): ProcessAnswerResultModel {
        quest as SelectBoolQuestUiModel

        return ProcessAnswerResultModel(
            quest = quest.copy(selectedAnswer = userAnswer),
            isLastAnswer = false,
        )
    }

    override fun getUserAnswers(
        domainQuest: BaseQuestDomainModel,
        quest: BaseQuestUiModel,
    ): Set<String> {
        quest as SelectBoolQuestUiModel

        return setOf(quest.selectedAnswer).filterNotNull().toSet()
    }

    override fun getQuestUiModel(domainQuest: BaseQuestDomainModel): BaseQuestUiModel {
        domainQuest as SelectBoolQuestModel

        val mapperArgs = SelectBoolQuestUiMapper.SelectBoolArgs(
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
        quest as SelectBoolQuestUiModel

        return quest.copy(
            highlight = highlight,
            answerButtonIsEnabled = (args as SelectBoolGameViewModelDelegateArgs).answerButtonIsEnabled,
        )
    }

    override fun getArgs(
        domainQuest: BaseQuestDomainModel,
        userAnswer: String,
        answerButtonIsEnabled: Boolean,
    ): GameViewModelDelegateArgs {
        return SelectBoolGameViewModelDelegateArgs(answerButtonIsEnabled = answerButtonIsEnabled)
    }

    data class SelectBoolGameViewModelDelegateArgs(
        val answerButtonIsEnabled: Boolean,
    ) : GameViewModelDelegateArgs
}
