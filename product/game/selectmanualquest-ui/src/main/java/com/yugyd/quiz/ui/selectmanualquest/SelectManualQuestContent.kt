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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.yugyd.quiz.ui.game.api.model.HighlightUiModel
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.QuizBackground
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme
import com.yugyd.quiz.uikit.theme.app_color_negative
import com.yugyd.quiz.uikit.theme.app_color_positive

private val HorizontalPadding = 16.dp

@Composable
fun SelectManualQuestContent(
    quest: SelectManualQuestUiModel,
    onAnswerSelected: (String, Boolean) -> Unit,
    onAnswerClicked: () -> Unit,
) {
    Column {
        Text(
            text = quest.questModel.questText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HorizontalPadding),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
        )

        HorizontalDivider(Modifier.padding(vertical = 16.dp))

        AnswerButtonsInternal(
            highlight = quest.highlight,
            answers = quest.answers,
            onAnswerSelected = onAnswerSelected,
            answerButtonIsEnabled = quest.answerButtonIsEnabled,
        )

        Spacer(
            modifier = Modifier.height(height = 16.dp),
        )

        Button(
            modifier = Modifier
                .padding(horizontal = HorizontalPadding)
                .align(Alignment.End),
            onClick = onAnswerClicked,
            enabled = quest.answerButtonIsEnabled,
        ) {
            Text(
                text = stringResource(id = R.string.select_manual_action_continue),
            )
        }
    }
}

@Composable
fun QuestTextInternal(quest: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = HorizontalPadding),
        text = quest,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.titleLarge,
    )
}

@Composable
fun AnswerButtonsInternal(
    highlight: HighlightUiModel,
    answers: List<SelectManualQuestAnswerUiModel>,
    answerButtonIsEnabled: Boolean,
    onAnswerSelected: (String, Boolean) -> Unit,
) {
    answers.forEachIndexed { index, answer ->
        val answerIndex = remember(index) {
            index
        }
        AnswerButton(
            answer = answer.text,
            textColor = getButtonColor(highlight, answerIndex),
            isEnabled = answerButtonIsEnabled,
            onAnswerChecked = {
                onAnswerSelected(answer.text, it)
            },
            isChecked = answer.isSelected,
        )
    }
}

private fun getButtonColor(
    highlight: HighlightUiModel,
    buttonIndex: Int,
): Color? {
    return when (highlight) {
        HighlightUiModel.Default -> null

        is HighlightUiModel.False -> {
            when {
                highlight.trueAnswerIndexes.contains(buttonIndex) -> app_color_positive
                highlight.falseIndexes.contains(buttonIndex) -> app_color_negative
                else -> null
            }
        }

        is HighlightUiModel.True -> {
            if (highlight.trueAnswerIndexes.contains(buttonIndex)) {
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
    isChecked: Boolean,
    textColor: Color?,
    isEnabled: Boolean,
    onAnswerChecked: (Boolean) -> Unit,
) {
    val listItemColors = if (textColor != null) {
        ListItemDefaults.colors(headlineColor = textColor)
    } else {
        ListItemDefaults.colors()
    }
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isEnabled) {
                onAnswerChecked(isChecked.not())
            },
        headlineContent = {
            Text(text = answer)
        },
        leadingContent = {
            Checkbox(
                checked = isChecked,
                onCheckedChange = onAnswerChecked,
                enabled = isEnabled,
            )
        },
        colors = listItemColors,
    )
}

@ThemePreviews
@Composable
private fun ContentPreview(
    @PreviewParameter(SelectManualQuestPreviewParameterProvider::class) item: SelectManualQuestUiModel,
) {
    QuizApplicationTheme {
        QuizBackground {
            SelectManualQuestContent(
                quest = item,
                onAnswerSelected = { _, _ -> },
                onAnswerClicked = {},
            )
        }
    }
}
