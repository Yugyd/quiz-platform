/*
 *    Copyright 2024 Roman Likhachev
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.yugyd.quiz.ui.enteraiquest

import com.yugyd.quiz.domain.enterai.EnterAiQuestModel
import com.yugyd.quiz.ui.enteraiquest.EnterAiQuestUiMapper.EnterAiArgs
import com.yugyd.quiz.ui.game.api.mapper.BaseQuestUiMapper
import com.yugyd.quiz.ui.game.api.mapper.UiMapperArgs
import com.yugyd.quiz.ui.game.api.model.HighlightUiModel
import com.yugyd.quiz.ui.game.api.model.QuestValueUiModel
import javax.inject.Inject

class EnterAiQuestUiMapper @Inject constructor() :
    BaseQuestUiMapper<EnterAiQuestModel, EnterAiQuestUiModel, EnterAiArgs> {

    override fun map(
        model: EnterAiQuestModel,
        args: EnterAiArgs
    ): EnterAiQuestUiModel {
        return EnterAiQuestUiModel(
            questModel = QuestValueUiModel(model.quest),
            userAnswer = args.userAnswer,
            isFieldEnabled = args.highlightModel == HighlightUiModel.Default,
            answerState = when (args.highlightModel) {
                HighlightUiModel.Default -> EnterAiQuestUiModel.AnswerState.NONE
                is HighlightUiModel.False -> EnterAiQuestUiModel.AnswerState.FAILED
                is HighlightUiModel.True -> EnterAiQuestUiModel.AnswerState.SUCCESS
            },
            answerDescriptionFromAi = when (args.highlightModel) {
                HighlightUiModel.Default, is HighlightUiModel.True -> ""
                is HighlightUiModel.False -> model.trueAnswers.first()
            },
            answerButtonIsEnabled = args.answerButtonIsEnabled,
        )
    }

    data class EnterAiArgs(
        val userAnswer: String,
        val highlightModel: HighlightUiModel,
        val answerButtonIsEnabled: Boolean,
    ) : UiMapperArgs
}
