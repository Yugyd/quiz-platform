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

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.yugyd.quiz.ui.game.api.model.HighlightUiModel
import com.yugyd.quiz.ui.game.api.model.QuestValueUiModel

internal class SelectManualQuestPreviewParameterProvider :
    PreviewParameterProvider<SelectManualQuestUiModel> {

    override val values: Sequence<SelectManualQuestUiModel> = sequenceOf(createQuest())

    private fun createQuest() = SelectManualQuestUiModel(
        questModel = QuestValueUiModel("Quest"),
        answers = listOf(
            SelectManualQuestAnswerUiModel(
                text = "Answer 1",
                isSelected = true,
            ),
            SelectManualQuestAnswerUiModel(
                text = "Answer 2",
                isSelected = false,
            ),
            SelectManualQuestAnswerUiModel(
                text = "Answer 3",
                isSelected = false,
            ),
            SelectManualQuestAnswerUiModel(
                text = "Answer 4",
                isSelected = false,
            ),
            SelectManualQuestAnswerUiModel(
                text = "Answer 4",
                isSelected = false,
            ),
        ),
        highlight = HighlightUiModel.Default,
        answerButtonIsEnabled = true,
    )
}
