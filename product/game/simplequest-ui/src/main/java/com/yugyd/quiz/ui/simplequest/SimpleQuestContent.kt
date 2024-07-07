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

package com.yugyd.quiz.ui.simplequest

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.yugyd.quiz.ui.game.api.model.HighlightUiModel
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.QuizBackground
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme
import com.yugyd.quiz.uikit.theme.app_color_negative
import com.yugyd.quiz.uikit.theme.app_color_positive

@Composable
fun SimpleQuestContent(
    quest: SimpleQuestUiModel,
    onAnswerClicked: (Int) -> Unit,
) {
    Column {
        Text(
            text = quest.quest,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
        )

        HorizontalDivider(Modifier.padding(vertical = 16.dp))

        AnswerButtons(
            highlight = quest.highlight,
            oneAnswer = quest.oneAnswer,
            twoAnswer = quest.twoAnswer,
            threeAnswer = quest.threeAnswer,
            fourAnswer = quest.fourAnswer,
            onAnswerClicked = onAnswerClicked,
            answerButtonIsEnabled = quest.answerButtonIsEnabled,
        )
    }
}

private const val ONE_ANSWER_INDEX = 0
private const val TWO_ANSWER_INDEX = 1
private const val THREE_ANSWER_INDEX = 2
private const val FOUR_ANSWER_INDEX = 3

@Composable
internal fun AnswerButtons(
    highlight: HighlightUiModel,
    oneAnswer: String,
    twoAnswer: String,
    threeAnswer: String,
    fourAnswer: String,
    answerButtonIsEnabled: Boolean,
    onAnswerClicked: (Int) -> Unit,
) {
    AnswerButton(
        answer = oneAnswer,
        textColor = getButtonColor(highlight, ONE_ANSWER_INDEX),
        isEnabled = answerButtonIsEnabled,
        onAnswerClicked = {
            onAnswerClicked(ONE_ANSWER_INDEX)
        }
    )

    AnswerButton(
        answer = twoAnswer,
        textColor = getButtonColor(highlight, TWO_ANSWER_INDEX),
        isEnabled = answerButtonIsEnabled,
        onAnswerClicked = {
            onAnswerClicked(TWO_ANSWER_INDEX)
        }
    )

    AnswerButton(
        answer = threeAnswer,
        textColor = getButtonColor(highlight, THREE_ANSWER_INDEX),
        isEnabled = answerButtonIsEnabled,
        onAnswerClicked = {
            onAnswerClicked(THREE_ANSWER_INDEX)
        }
    )

    AnswerButton(
        answer = fourAnswer,
        textColor = getButtonColor(highlight, FOUR_ANSWER_INDEX),
        isEnabled = answerButtonIsEnabled,
        onAnswerClicked = {
            onAnswerClicked(FOUR_ANSWER_INDEX)
        }
    )
}

private fun getButtonColor(
    highlight: HighlightUiModel,
    buttonIndex: Int,
): Color? {
    return when (highlight) {
        HighlightUiModel.Default -> null

        is HighlightUiModel.False -> {
            when (buttonIndex) {
                highlight.trueIndex -> {
                    app_color_positive
                }

                highlight.falseIndex -> {
                    app_color_negative
                }

                else -> {
                    null
                }
            }
        }

        is HighlightUiModel.True -> {
            if (buttonIndex == highlight.index) {
                app_color_positive
            } else {
                null
            }
        }
    }
}

@Composable
internal fun AnswerButton(
    answer: String,
    textColor: Color?,
    isEnabled: Boolean,
    onAnswerClicked: () -> Unit,
) {
    val buttonColors = if (textColor != null) {
        ButtonDefaults.textButtonColors(contentColor = textColor)
    } else {
        ButtonDefaults.textButtonColors()
    }
    val horizontalMargin = 16.dp
    TextButton(
        onClick = {
            if (isEnabled) {
                onAnswerClicked()
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = buttonColors,
        shape = RectangleShape,
        contentPadding = PaddingValues(
            start = horizontalMargin,
            top = ButtonDefaults.TextButtonContentPadding.calculateTopPadding(),
            end = horizontalMargin,
            bottom = ButtonDefaults.TextButtonContentPadding.calculateBottomPadding(),
        ),
    ) {
        Text(
            text = answer,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@ThemePreviews
@Composable
private fun ContentPreview(
    @PreviewParameter(SimpleQuestPreviewParameterProvider::class) item: SimpleQuestUiModel,
) {
    QuizApplicationTheme {
        QuizBackground {
            SimpleQuestContent(
                quest = item,
                onAnswerClicked = {},
            )
        }
    }
}
