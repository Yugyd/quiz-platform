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

package com.yugyd.quiz.ui.selectmanualquest

import com.yugyd.quiz.domain.selectmanualquest.SelectManualQuestModel
import com.yugyd.quiz.ui.game.api.mapper.BaseQuestUiMapper
import com.yugyd.quiz.ui.game.api.mapper.UiMapperArgs
import com.yugyd.quiz.ui.game.api.model.HighlightUiModel
import com.yugyd.quiz.ui.game.api.model.QuestValueUiModel
import com.yugyd.quiz.ui.selectmanualquest.SelectManualQuestUiMapper.SelectManualQuestsArgs
import javax.inject.Inject

class SelectManualQuestUiMapper @Inject constructor() :
    BaseQuestUiMapper<SelectManualQuestModel, SelectManualQuestUiModel, SelectManualQuestsArgs> {

    override fun map(
        model: SelectManualQuestModel,
        args: SelectManualQuestsArgs,
    ): SelectManualQuestUiModel {
        val answers = model.answers.map {
            SelectManualQuestAnswerUiModel(
                text = it,
                isSelected = false,
            )
        }
        return SelectManualQuestUiModel(
            questModel = QuestValueUiModel(model.quest),
            answers = answers,
            answerButtonIsEnabled = true,
            highlight = HighlightUiModel.Default,
        )
    }

    data object SelectManualQuestsArgs : UiMapperArgs
}
