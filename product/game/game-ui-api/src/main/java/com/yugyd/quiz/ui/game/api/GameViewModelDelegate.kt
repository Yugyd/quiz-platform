package com.yugyd.quiz.ui.game.api

import com.yugyd.quiz.domain.game.api.BaseQuestDomainModel
import com.yugyd.quiz.ui.game.api.model.BaseQuestUiModel
import com.yugyd.quiz.ui.game.api.model.HighlightUiModel
import com.yugyd.quiz.ui.game.api.model.ProcessAnswerResultModel

interface GameViewModelDelegate {

    /**
     * Determines whether a certain question type can be handled. Only used at the di level.
     */
    fun isTypeHandled(domainQuest: BaseQuestDomainModel): Boolean

    /**
     * Returns a list of answers for a question. Used when you need to process the results.
     */
    fun getUserAnswers(
        domainQuest: BaseQuestDomainModel,
        quest: BaseQuestUiModel,
    ): Set<String>

    /**
     * Processes a user response, returns a new question state with user responses. Used when you
     * need to process a response event from a user.
     */
    fun processUserAnswer(
        domainQuest: BaseQuestDomainModel,
        quest: BaseQuestUiModel,
        userAnswer: String,
        isSelected: Boolean,
    ): ProcessAnswerResultModel

    fun getQuestUiModel(domainQuest: BaseQuestDomainModel): BaseQuestUiModel

    fun updateQuestUiModel(
        domainQuest: BaseQuestDomainModel,
        quest: BaseQuestUiModel,
        highlight: HighlightUiModel,
        args: GameViewModelDelegateArgs?,
    ): BaseQuestUiModel

    fun getArgs(
        domainQuest: BaseQuestDomainModel,
        userAnswer: String,
        answerButtonIsEnabled: Boolean,
    ): GameViewModelDelegateArgs?

    interface GameViewModelDelegateArgs
}
