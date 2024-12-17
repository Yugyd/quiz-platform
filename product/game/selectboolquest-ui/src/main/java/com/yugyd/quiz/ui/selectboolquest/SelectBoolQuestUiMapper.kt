/*
 *    Copyright 2023 Roman Likhachev
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

package com.yugyd.quiz.ui.selectboolquest

import com.yugyd.quiz.domain.selectboolquest.SelectBoolQuestModel
import com.yugyd.quiz.ui.game.api.mapper.BaseQuestUiMapper
import com.yugyd.quiz.ui.game.api.mapper.UiMapperArgs
import com.yugyd.quiz.ui.game.api.model.HighlightUiModel
import com.yugyd.quiz.ui.game.api.model.QuestValueUiModel
import com.yugyd.quiz.ui.selectboolquest.SelectBoolQuestUiMapper.SelectBoolArgs
import javax.inject.Inject

class SelectBoolQuestUiMapper @Inject constructor() :
    BaseQuestUiMapper<SelectBoolQuestModel, SelectBoolQuestUiModel, SelectBoolArgs> {

    override fun map(model: SelectBoolQuestModel, args: SelectBoolArgs): SelectBoolQuestUiModel {
        return SelectBoolQuestUiModel(
            questModel = QuestValueUiModel(
                questText = model.quest,
                imageUri = model.image,
            ),
            answers = model.answers,
            selectedAnswer = null,
            answerButtonIsEnabled = args.answerButtonIsEnabled,
            highlight = args.highlight,
        )
    }

    data class SelectBoolArgs(
        val answerButtonIsEnabled: Boolean,
        val highlight: HighlightUiModel,
    ) : UiMapperArgs
}
