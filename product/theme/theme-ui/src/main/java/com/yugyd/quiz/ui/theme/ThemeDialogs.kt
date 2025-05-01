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

package com.yugyd.quiz.ui.theme

import android.net.Uri
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.yugyd.quiz.ui.theme.model.RewardDialogUiModel
import com.yugyd.quiz.ui.theme.model.RewardType
import com.yugyd.quiz.ui.theme.model.ThemeUiModel
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.QuizBackground
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme
import com.yugyd.quiz.uikit.theme.app_color_positive
import com.yugyd.quiz.uikit.R as UiKitR

@Composable
internal fun InfoDialog(
    model: ThemeUiModel,
    onStartClicked: (ThemeUiModel) -> Unit,
    onInfoDialogDismissState: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onInfoDialogDismissState()
        },
        title = {
            Text(
                text = model.title,
            )
        },
        text = {
            Text(
                text = model.subtitle,
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onStartClicked(model)
                    onInfoDialogDismissState()
                }
            ) {
                Text(stringResource(id = UiKitR.string.ds_action_game))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onInfoDialogDismissState()
                }
            ) {
                Text(stringResource(id = R.string.action_close))
            }
        }
    )
}

@Composable
internal fun RewardedDialog(
    model: RewardDialogUiModel,
    onPositiveRewardDialogClicked: (RewardDialogUiModel) -> Unit,
    onNegativeRewardDialogClicked: () -> Unit,
    onRewardDialogDismissState: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            onRewardDialogDismissState()
        },
        title = {
            Text(
                text = model.title,
            )
        },
        text = {
            Text(
                text = model.message,
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onPositiveRewardDialogClicked(model)
                    onRewardDialogDismissState()
                }
            ) {
                Text(text = model.positiveButton)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onNegativeRewardDialogClicked()
                    onRewardDialogDismissState()
                }
            ) {
                Text(text = model.negativeButton)
            }
        }
    )
}

@Composable
internal fun ResetDialog(
    model: ThemeUiModel,
    onPositiveResetDialogClicked: (ThemeUiModel) -> Unit,
    onResetDialogDismissState: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            onResetDialogDismissState()
        },
        title = {
            Text(
                text = stringResource(id = R.string.title_theme_completed),
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onPositiveResetDialogClicked(model)
                    onResetDialogDismissState()
                }
            ) {
                Text(text = stringResource(id = android.R.string.ok))
            }
        },
    )
}

private val testDialog = ThemeUiModel(
    id = 0,
    title = "Title",
    subtitle = "Subtitle",
    imageUri = Uri.EMPTY,
    progressPercent = 40,
    record = 40,
    progressColor = app_color_positive,
)

@ThemePreviews
@Composable
private fun InfoDialogPreview() {
    QuizApplicationTheme {
        QuizBackground {
            InfoDialog(
                model = testDialog,
                onStartClicked = {},
                onInfoDialogDismissState = {},
            )
        }
    }
}

@ThemePreviews
@Composable
private fun RewardedDialogPreview() {
    QuizApplicationTheme {
        QuizBackground {
            val rewardedDialogUiModel = RewardDialogUiModel(
                title = "Title",
                message = "Message",
                positiveButton = "Positive",
                negativeButton = "Negative",
                rewardType = RewardType.Ad,
                themeUiModel = testDialog,
            )

            RewardedDialog(
                model = rewardedDialogUiModel,
                onPositiveRewardDialogClicked = {},
                onNegativeRewardDialogClicked = {},
                onRewardDialogDismissState = {},
            )
        }
    }
}

@ThemePreviews
@Composable
private fun ResetDialogPreview() {
    QuizApplicationTheme {
        QuizBackground {
            ResetDialog(
                model = testDialog,
                onPositiveResetDialogClicked = {},
                onResetDialogDismissState = {},
            )
        }
    }
}
