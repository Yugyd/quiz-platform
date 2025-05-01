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

package com.yugyd.quiz.progressui.specificprogress

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yugyd.quiz.progressui.R
import com.yugyd.quiz.progressui.model.ProgressUiModel
import com.yugyd.quiz.progressui.progress.ProgressContent
import com.yugyd.quiz.progressui.shared.ProgressPreviewParameterProvider
import com.yugyd.quiz.progressui.specificprogress.SpecificProgressView.Action
import com.yugyd.quiz.progressui.specificprogress.SpecificProgressView.State
import com.yugyd.quiz.progressui.specificprogress.SpecificProgressView.State.NavigationState
import com.yugyd.quiz.progressui.specificprogress.SpecificProgressView.State.ResetState
import com.yugyd.quiz.uikit.LoadingContent
import com.yugyd.quiz.uikit.WarningContent
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.QuizBackground
import com.yugyd.quiz.uikit.component.SimpleToolbar
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme
import com.yugyd.quiz.uikit.R as UiKitR

@Composable
internal fun SpecificProgressRoute(
    viewModel: SpecificProgressViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SpecificProgressScreen(
        uiState = state,
        snackbarHostState = snackbarHostState,
        onBackPressed = {
            viewModel.onAction(Action.OnBackPressed)
        },
        onRightIconClicked = {
            viewModel.onAction(Action.OnResetProgress)
        },
        onResetButtonClicked = {
            viewModel.onAction(Action.OnResetProgressAccepted)
        },
        onResetDialogDismissState = {
            viewModel.onAction(Action.OnResetDialogDismissed)
        },
        onErrorDismissState = {
            viewModel.onAction(Action.OnSnackbarDismissed)
        },
        onBack = onBack,
        onNavigationHandled = {
            viewModel.onAction(Action.OnNavigationHandled)
        },
    )
}

@Composable
internal fun SpecificProgressScreen(
    uiState: State,
    snackbarHostState: SnackbarHostState,
    onBackPressed: () -> Unit,
    onRightIconClicked: () -> Unit,
    onResetButtonClicked: () -> Unit,
    onResetDialogDismissState: () -> Unit,
    onErrorDismissState: () -> Unit,
    onBack: () -> Unit,
    onNavigationHandled: () -> Unit,
) {
    if (uiState.showErrorMessage) {
        val showSnackbarMessage = stringResource(id = UiKitR.string.ds_error_base)
        LaunchedEffect(showSnackbarMessage, snackbarHostState) {
            snackbarHostState.showSnackbar(message = showSnackbarMessage)
            onErrorDismissState()
        }
    }

    if (uiState.showResetDialog) {
        ResetAlertDialog(
            onResetButtonClicked = onResetButtonClicked,
            onResetDialogDismissState = onResetDialogDismissState,
        )
    }

    Column {
        SpecificProgressToolbar(
            themeTitle = uiState.themeTitle,
            resetState = uiState.resetState,
            onBackPressed = onBackPressed,
            onRightIconClicked = onRightIconClicked,
        )

        when {
            uiState.isLoading -> {
                LoadingContent()
            }

            uiState.isWarning -> {
                WarningContent()
            }

            else -> {
                ProgressContent(
                    items = uiState.items,
                    onItemClicked = null,
                )
            }
        }
    }

    NavigationHandler(
        navigationState = uiState.navigationState,
        onBack = onBack,
        onNavigationHandled = onNavigationHandled,
    )
}

@Composable
private fun ResetAlertDialog(
    onResetButtonClicked: () -> Unit,
    onResetDialogDismissState: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onResetDialogDismissState()
        },
        icon = {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = null,
            )
        },
        title = {
            Text(
                text = stringResource(id = R.string.title_reset_dialog_progress)
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.title_reset_dialog_progress_info)
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onResetButtonClicked()
                    onResetDialogDismissState()
                }
            ) {
                Text(stringResource(id = R.string.action_reset_dialog_yes))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onResetDialogDismissState()
                }
            ) {
                Text(stringResource(id = R.string.action_reset_dialog_no))
            }
        }
    )
}

@Composable
private fun SpecificProgressToolbar(
    themeTitle: String,
    resetState: ResetState,
    onBackPressed: () -> Unit,
    onRightIconClicked: () -> Unit,
) {
    val rightIconButtonEnabled = when (resetState) {
        ResetState.VISIBLE -> true
        ResetState.HIDE -> false
    }
    SimpleToolbar(
        title = themeTitle,
        onBackPressed = onBackPressed,
        rightIcon = Icons.Outlined.Delete,
        rightIconButtonEnabled = rightIconButtonEnabled,
        onRightIconClicked = onRightIconClicked
    )
}

@Composable
internal fun NavigationHandler(
    navigationState: NavigationState?,
    onBack: () -> Unit,
    onNavigationHandled: () -> Unit,
) {
    LaunchedEffect(key1 = navigationState) {
        when (navigationState) {
            NavigationState.Back -> onBack()
            null -> Unit
        }

        navigationState?.let { onNavigationHandled() }
    }
}

@ThemePreviews
@Composable
private fun ContentPreview(
    @PreviewParameter(ProgressPreviewParameterProvider::class) items: List<ProgressUiModel>,
) {
    QuizApplicationTheme {
        QuizBackground {
            ProgressContent(
                items = items,
                onItemClicked = {},
            )
        }
    }
}
