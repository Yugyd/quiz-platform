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

package com.yugyd.quiz.ui.enterquest

import androidx.core.text.isDigitsOnly
import com.yugyd.quiz.domain.enterquest.EnterQuestModel
import com.yugyd.quiz.ui.enterquest.EnterQuestUiMapper.EnterArgs
import com.yugyd.quiz.ui.game.api.mapper.BaseQuestUiMapper
import com.yugyd.quiz.ui.game.api.mapper.UiMapperArgs
import com.yugyd.quiz.ui.game.api.model.HighlightUiModel
import com.yugyd.quiz.ui.game.api.model.QuestValueUiModel
import javax.inject.Inject

class EnterQuestUiMapper @Inject constructor() :
    BaseQuestUiMapper<EnterQuestModel, EnterQuestUiModel, EnterArgs> {

    override fun map(model: EnterQuestModel, args: EnterArgs): EnterQuestUiModel {
        val isNumberKeyboard = model.trueAnswers.first().isDigitsOnly()

        return EnterQuestUiModel(
            questModel = QuestValueUiModel(model.quest),
            userAnswer = args.userAnswer,
            isNumberKeyboard = isNumberKeyboard,
            isFieldEnabled = args.highlightModel == HighlightUiModel.Default,
            answerState = when (args.highlightModel) {
                HighlightUiModel.Default -> EnterQuestUiModel.AnswerState.NONE
                is HighlightUiModel.False -> EnterQuestUiModel.AnswerState.FAILED
                is HighlightUiModel.True -> EnterQuestUiModel.AnswerState.SUCCESS
            },
            trueAnswer = when (args.highlightModel) {
                HighlightUiModel.Default, is HighlightUiModel.True -> ""
                is HighlightUiModel.False -> model.trueAnswers.first()
            },
        )
    }

    data class EnterArgs(
        val userAnswer: String,
        val highlightModel: HighlightUiModel,
    ) : UiMapperArgs
}
