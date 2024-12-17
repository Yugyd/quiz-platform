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

package com.yugyd.quiz.ui.selectmanualquest.viewmodel

import com.yugyd.quiz.domain.game.api.BaseQuestDomainModel
import com.yugyd.quiz.domain.selectmanualquest.SelectManualQuestModel
import com.yugyd.quiz.ui.game.api.GameViewModelDelegate
import com.yugyd.quiz.ui.game.api.GameViewModelDelegate.GameViewModelDelegateArgs
import com.yugyd.quiz.ui.game.api.model.BaseQuestUiModel
import com.yugyd.quiz.ui.game.api.model.HighlightUiModel
import com.yugyd.quiz.ui.game.api.model.ProcessAnswerResultModel
import com.yugyd.quiz.ui.selectmanualquest.SelectManualQuestAnswerUiModel
import com.yugyd.quiz.ui.selectmanualquest.SelectManualQuestUiMapper
import com.yugyd.quiz.ui.selectmanualquest.SelectManualQuestUiModel
import javax.inject.Inject

class SelectManualGameViewModelDelegate @Inject constructor(
    private val simpleQuestUiMapper: SelectManualQuestUiMapper,
) : GameViewModelDelegate {

    override fun isTypeHandled(domainQuest: BaseQuestDomainModel): Boolean {
        return domainQuest is SelectManualQuestModel
    }

    override fun getUserAnswers(
        domainQuest: BaseQuestDomainModel,
        quest: BaseQuestUiModel,
    ): Set<String> {
        return (quest as SelectManualQuestUiModel).answers
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
        quest as SelectManualQuestUiModel

        val newAnswers = quest.answers.map {
            if (it.text == userAnswer) {
                it.copy(
                    isSelected = isSelected,
                )
            } else {
                it
            }
        }
        return ProcessAnswerResultModel(
            quest = quest.copy(answers = newAnswers),
            isLastAnswer = false,
        )
    }

    override fun getQuestUiModel(domainQuest: BaseQuestDomainModel): BaseQuestUiModel {
        domainQuest as SelectManualQuestModel

        return simpleQuestUiMapper.map(
            model = domainQuest,
            args = SelectManualQuestUiMapper.SelectManualQuestsArgs,
        )
    }

    override fun updateQuestUiModel(
        domainQuest: BaseQuestDomainModel,
        quest: BaseQuestUiModel,
        highlight: HighlightUiModel,
        args: GameViewModelDelegateArgs?
    ): BaseQuestUiModel {
        quest as SelectManualQuestUiModel
        args as SelectManualGameViewModelDelegateArgs

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
        return SelectManualGameViewModelDelegateArgs(answerButtonIsEnabled = answerButtonIsEnabled)
    }

    data class SelectManualGameViewModelDelegateArgs(
        val answerButtonIsEnabled: Boolean,
    ) : GameViewModelDelegateArgs
}
