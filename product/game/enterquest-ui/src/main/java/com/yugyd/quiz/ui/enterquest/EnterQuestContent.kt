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

package com.yugyd.quiz.ui.enterquest

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.yugyd.quiz.ui.enterquest.EnterQuestUiModel.AnswerState
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.QuizBackground
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme

@Composable
fun EnterQuestContent(
    quest: EnterQuestUiModel,
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
        questHint = stringResource(id = R.string.enter_quest_correct_answer),
        manualAnswer = manualAnswer,
        trueAnswer = quest.trueAnswer,
        isErrorSupportingText = quest.answerState == AnswerState.FAILED,
        isFieldEnabled = quest.isFieldEnabled,
        onAnswerHandler = onAnswerHandler,
        onAnswerTextChanged = onAnswerTextChanged,
    )
}

@Composable
fun EnterQuestContentInternal(
    quest: String,
    isNumberKeyboard: Boolean,
    trailingIcon: ImageVector,
    manualAnswer: String,
    trueAnswer: String,
    questHint: String,
    isErrorSupportingText: Boolean,
    isFieldEnabled: Boolean,
    onAnswerHandler: () -> Unit,
    onAnswerTextChanged: (String) -> Unit,
) {
    val focusRequester = remember {
        FocusRequester()
    }

    Column(
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        val controlModifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)

        Text(
            text = quest,
            modifier = controlModifier,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        val keyboardType = if (isNumberKeyboard) {
            KeyboardType.Number
        } else {
            KeyboardType.Text
        }
        OutlinedTextField(
            modifier = controlModifier.focusRequester(focusRequester),
            value = manualAnswer,
            onValueChange = onAnswerTextChanged,
            label = {
                Text(text = questHint)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = ImeAction.Done,
            ),
            singleLine = true,
            keyboardActions = KeyboardActions(
                onAny = {
                    onAnswerHandler()
                },
            ),
            trailingIcon = {
                IconButton(
                    onClick = {
                        onAnswerTextChanged(CLEAR_TEXT_VALUE)
                    }
                ) {
                    Icon(
                        imageVector = trailingIcon,
                        contentDescription = null,
                    )
                }
            },
            isError = isErrorSupportingText,
            supportingText = {
                Text(text = trueAnswer)
            },
            readOnly = !isFieldEnabled,
        )

        Spacer(
            modifier = Modifier.height(height = 16.dp)
        )
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

private const val CLEAR_TEXT_VALUE = ""

@ThemePreviews
@Composable
private fun ContentPreview(
    @PreviewParameter(EnterQuestPreviewParameterProvider::class) item: EnterQuestUiModel,
) {
    QuizApplicationTheme {
        QuizBackground {
            EnterQuestContent(
                quest = item,
                manualAnswer = "",
                onAnswerHandler = {},
                onAnswerTextChanged = {},
            )
        }
    }
}
