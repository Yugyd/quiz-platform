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

package com.yugyd.quiz.ui.hintenterquest

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.yugyd.quiz.ui.enterquest.EnterQuestContentInternal
import com.yugyd.quiz.ui.hintenterquest.EnterWithHintQuestUiModel.AnswerState
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.QuizBackground
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme

@Composable
fun EnterWithHintQuestContent(
    quest: EnterWithHintQuestUiModel,
    manualAnswer: String,
    onAnswerHandler: () -> Unit,
    onAnswerTextChanged: (String) -> Unit,
) {
    EnterQuestContentInternal(
        quest = quest.questModel.questText,
        isNumberKeyboard = quest.isNumberKeyboard,
        trailingIcon = remember(quest.answerState) {
            when (quest.answerState) {
                AnswerState.SUCCESS -> Icons.Rounded.Done
                AnswerState.FAILED -> Icons.Rounded.Error
                AnswerState.NONE -> Icons.Rounded.Cancel
            }
        },
        questHint = quest.answerHint,
        manualAnswer = manualAnswer,
        trueAnswer = quest.trueAnswer,
        isErrorSupportingText = quest.answerState == AnswerState.FAILED,
        isFieldEnabled = quest.isFieldEnabled,
        onAnswerHandler = onAnswerHandler,
        onAnswerTextChanged = onAnswerTextChanged,
    )
}

@ThemePreviews
@Composable
private fun ContentPreview(
    @PreviewParameter(EnterWithHintQuestPreviewParameterProvider::class) item: EnterWithHintQuestUiModel,
) {
    QuizApplicationTheme {
        QuizBackground {
            EnterWithHintQuestContent(
                quest = item.copy(answerState = AnswerState.FAILED),
                manualAnswer = "User answer",
                onAnswerHandler = {},
                onAnswerTextChanged = {},
            )
        }
    }
}
