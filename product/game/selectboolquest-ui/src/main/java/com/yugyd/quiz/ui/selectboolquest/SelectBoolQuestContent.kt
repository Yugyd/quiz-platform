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

package com.yugyd.quiz.ui.selectboolquest

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.yugyd.quiz.ui.simplequest.SimpleQuestContent
import com.yugyd.quiz.ui.simplequest.SimpleQuestUiModel
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.QuizBackground
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme

@Composable
fun SelectBoolQuestContent(
    quest: SelectBoolQuestUiModel,
    onAnswerClicked: (String) -> Unit,
) {
    val simpleQuest = remember(quest) {
        SimpleQuestUiModel(
            questModel = quest.questModel,
            answers = quest.answers,
            answerButtonIsEnabled = quest.answerButtonIsEnabled,
            selectedAnswer = quest.selectedAnswer,
            highlight = quest.highlight,
        )
    }
    SimpleQuestContent(
        quest = simpleQuest,
        onAnswerClicked = onAnswerClicked,
    )
}

@ThemePreviews
@Composable
private fun ContentPreview(
    @PreviewParameter(SelectBoolQuestPreviewParameterProvider::class) item: SelectBoolQuestUiModel,
) {
    QuizApplicationTheme {
        QuizBackground {
            SelectBoolQuestContent(
                quest = item,
                onAnswerClicked = {},
            )
        }
    }
}
