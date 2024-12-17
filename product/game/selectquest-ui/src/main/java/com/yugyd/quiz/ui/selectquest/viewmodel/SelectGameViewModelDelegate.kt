/*
 * Copyright 2024 Roman Likhachev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yugyd.quiz.ui.selectquest.viewmodel

import com.yugyd.quiz.domain.game.api.BaseQuestDomainModel
import com.yugyd.quiz.domain.selectquest.SelectQuestModel
import com.yugyd.quiz.ui.game.api.GameViewModelDelegate
import com.yugyd.quiz.ui.game.api.GameViewModelDelegate.GameViewModelDelegateArgs
import com.yugyd.quiz.ui.game.api.model.BaseQuestUiModel
import com.yugyd.quiz.ui.game.api.model.HighlightUiModel
import com.yugyd.quiz.ui.game.api.model.ProcessAnswerResultModel
import com.yugyd.quiz.ui.selectmanualquest.SelectManualQuestAnswerUiModel
import com.yugyd.quiz.ui.selectquest.SelectQuestUiMapper
import com.yugyd.quiz.ui.selectquest.SelectQuestUiModel
import javax.inject.Inject

class SelectGameViewModelDelegate @Inject constructor(
    private val simpleQuestUiMapper: SelectQuestUiMapper,
) : GameViewModelDelegate {

    override fun isTypeHandled(domainQuest: BaseQuestDomainModel): Boolean {
        return domainQuest is SelectQuestModel
    }

    override fun getUserAnswers(
        domainQuest: BaseQuestDomainModel,
        quest: BaseQuestUiModel,
    ): Set<String> {
        return (quest as SelectQuestUiModel).answers
            .filter(SelectManualQuestAnswerUiModel::isSelected)
            .map(SelectManualQuestAnswerUiModel::text)
            .toSet()
    }

    override fun processUserAnswer(
        domainQuest: BaseQuestDomainModel,
        quest: BaseQuestUiModel,
        userAnswer: String,
        isSelected: Boolean,
    ): ProcessAnswerResultModel {
        domainQuest as SelectQuestModel
        quest as SelectQuestUiModel

        val newAnswers = quest.answers.map {
            if (it.text == userAnswer) {
                it.copy(
                    isSelected = isSelected,
                )
            } else {
                it
            }
        }
        val selectedAnswerCount = newAnswers.count(SelectManualQuestAnswerUiModel::isSelected)
        return ProcessAnswerResultModel(
            quest = quest.copy(answers = newAnswers),
            isLastAnswer = domainQuest.trueAnswerCount == selectedAnswerCount,
        )
    }

    override fun getQuestUiModel(domainQuest: BaseQuestDomainModel): BaseQuestUiModel {
        domainQuest as SelectQuestModel

        return simpleQuestUiMapper.map(
            model = domainQuest,
            args = SelectQuestUiMapper.SelectQuestsArgs,
        )
    }

    override fun updateQuestUiModel(
        domainQuest: BaseQuestDomainModel,
        quest: BaseQuestUiModel,
        highlight: HighlightUiModel,
        args: GameViewModelDelegateArgs?
    ): BaseQuestUiModel {
        quest as SelectQuestUiModel
        args as SelectGameViewModelDelegateArgs

        return quest.copy(
            highlight = highlight,
            answerButtonIsEnabled = args.answerButtonIsEnabled,
        )
    }

    override fun getArgs(
        domainQuest: BaseQuestDomainModel,
        userAnswer: String,
        answerButtonIsEnabled: Boolean,
    ): GameViewModelDelegateArgs {
        return SelectGameViewModelDelegateArgs(answerButtonIsEnabled = answerButtonIsEnabled)
    }

    data class SelectGameViewModelDelegateArgs(
        val answerButtonIsEnabled: Boolean,
    ) : GameViewModelDelegateArgs
}
