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

package com.yugyd.quiz.ui.enteraiquest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.yugyd.quiz.ui.enteraiquest.EnterAiQuestUiModel.AnswerState
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.QuizBackground
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme

val controlModifier = Modifier
    .fillMaxWidth()
    .padding(horizontal = 16.dp)

@Composable
fun EnterAiQuestContent(
    quest: EnterAiQuestUiModel,
    manualAnswer: String,
    onAnswerHandler: () -> Unit,
    onAnswerTextChanged: (String) -> Unit,
) {
    val focusRequester = remember {
        FocusRequester()
    }

    val isError = remember(quest.answerState) {
        quest.answerState == AnswerState.FAILED
    }

    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Text(
            text = quest.questModel.questText,
            modifier = controlModifier,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        OutlinedTextField(
            modifier = controlModifier.focusRequester(focusRequester),
            value = manualAnswer,
            onValueChange = onAnswerTextChanged,
            label = {
                Text(
                    text = stringResource(id = R.string.enter_ai_title_enter_answer),
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
            minLines = 4,
            singleLine = false,
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
                        imageVector = remember(quest.answerState) {
                            when (quest.answerState) {
                                AnswerState.SUCCESS -> Icons.Rounded.Done
                                AnswerState.FAILED -> Icons.Rounded.Error
                                AnswerState.NONE -> Icons.Rounded.Cancel
                            }
                        },
                        contentDescription = null,
                    )
                }
            },
            isError = isError,
            supportingText = {
                Text(
                    text = stringResource(id = R.string.enter_ai_title_supporting),
                )
            },
            readOnly = !quest.isFieldEnabled,
        )

        when (quest.answerState) {
            AnswerState.SUCCESS -> Unit

            AnswerState.FAILED -> {
                Spacer(modifier = Modifier.height(height = 16.dp))

                AiAnswer(
                    aiDescription = quest.answerDescriptionFromAi.ifEmpty {
                        stringResource(id = R.string.enter_ai_check_error)
                    },
                )
            }

            AnswerState.NONE -> {
                Spacer(
                    modifier = Modifier.height(height = 16.dp),
                )

                ContinueButton(
                    onAnswerClicked = onAnswerHandler,
                    answerButtonIsEnabled = quest.answerButtonIsEnabled,
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun ColumnScope.ContinueButton(
    onAnswerClicked: () -> Unit,
    answerButtonIsEnabled: Boolean,
) {
    Button(
        modifier = Modifier
            .padding(end = 16.dp)
            .align(Alignment.End),
        onClick = onAnswerClicked,
        enabled = answerButtonIsEnabled,
    ) {
        Text(
            text = stringResource(id = R.string.enter_ai_continue),
        )
    }
}

@Composable
private fun AiAnswer(aiDescription: String) {
    Column(modifier = controlModifier) {
        Text(
            text = stringResource(id = R.string.enter_ai_title_true_answer_description_from_ai),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelMedium,
        )

        Spacer(
            modifier = Modifier.height(8.dp),
        )

        Text(
            text = aiDescription,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

private const val CLEAR_TEXT_VALUE = ""

@ThemePreviews
@Composable
private fun ContentPreview(
    @PreviewParameter(EnterAiQuestPreviewParameterProvider::class) item: EnterAiQuestUiModel,
) {
    QuizApplicationTheme {
        QuizBackground {
            EnterAiQuestContent(
                quest = item.copy(answerState = AnswerState.FAILED, answerDescriptionFromAi = ""),
                manualAnswer = "User answer",
                onAnswerHandler = {},
                onAnswerTextChanged = {},
            )
        }
    }
}
