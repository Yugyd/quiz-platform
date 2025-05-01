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

package com.yugyd.quiz.correctui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yugyd.quiz.correctui.CorrectView.Action
import com.yugyd.quiz.correctui.CorrectView.State
import com.yugyd.quiz.correctui.CorrectView.State.AvailableMode
import com.yugyd.quiz.correctui.CorrectView.State.NavigationState
import com.yugyd.quiz.domain.api.model.payload.GamePayload
import com.yugyd.quiz.uikit.LoadingContent
import com.yugyd.quiz.uikit.WarningContent
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.IconWithBackground
import com.yugyd.quiz.uikit.component.QuizBackground
import com.yugyd.quiz.uikit.component.RootToolbar
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme
import com.yugyd.quiz.uikit.R as UiKitR

@Composable
internal fun CorrectRoute(
    viewModel: CorrectViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    onNavigateToGame: (GamePayload) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CorrectScreen(
        uiState = state,
        snackbarHostState = snackbarHostState,
        onGameButtonClicked = {
            viewModel.onAction(Action.OnStartClicked)
        },
        onErrorDismissState = {
            viewModel.onAction(Action.OnSnackbarDismissed)
        },
        onNavigateToGame = onNavigateToGame,
        onNavigationHandled = {
            viewModel.onAction(Action.OnNavigationHandled)
        },
    )
}

@Composable
internal fun CorrectScreen(
    uiState: State,
    snackbarHostState: SnackbarHostState,
    onGameButtonClicked: () -> Unit,
    onErrorDismissState: () -> Unit,
    onNavigateToGame: (GamePayload) -> Unit,
    onNavigationHandled: () -> Unit,
) {
    val errorMessage = stringResource(id = UiKitR.string.ds_error_base)
    LaunchedEffect(key1 = uiState.showErrorMessage) {
        if (uiState.showErrorMessage) {
            snackbarHostState.showSnackbar(message = errorMessage)

            onErrorDismissState()
        }
    }

    Column {
        RootToolbar(
            title = stringResource(id = UiKitR.string.ds_navbar_title_correct),
        )

        when {
            uiState.isLoading -> {
                LoadingContent()
            }

            uiState.isWarning -> {
                WarningContent()
            }

            else -> {
                CorrectContent(
                    availableMode = uiState.availableMode,
                    icon = R.drawable.ic_thumb_up_48,
                    showIconBackground = true,
                    isStartButtonEnabled = uiState.isHaveErrors,
                    onStartButtonClicked = onGameButtonClicked,
                )
            }
        }
    }

    NavigationHandler(
        navigationState = uiState.navigationState,
        onNavigateToGame = onNavigateToGame,
        onNavigationHandled = onNavigationHandled,
    )
}

@Composable
internal fun CorrectContent(
    availableMode: AvailableMode,
    @DrawableRes icon: Int,
    showIconBackground: Boolean,
    isStartButtonEnabled: Boolean,
    onStartButtonClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (showIconBackground) {
            IconWithBackground(
                modifier = Modifier.size(size = 96.dp),
                icon = icon,
                contentDescription = null,
            )
        } else {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(96.dp),
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(id = R.string.correct_title_remember_quest),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.correct_msg_correct_quest),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (availableMode == AvailableMode.GAME_BUTTON) {
            Button(
                onClick = onStartButtonClicked,
                enabled = isStartButtonEnabled
            ) {
                Text(text = stringResource(id = UiKitR.string.ds_action_game))
            }
        }

        if (availableMode == AvailableMode.PRO_MESSAGE) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.correct_msg_correct_pro_available),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
internal fun NavigationHandler(
    navigationState: NavigationState?,
    onNavigateToGame: (GamePayload) -> Unit,
    onNavigationHandled: () -> Unit,
) {
    LaunchedEffect(key1 = navigationState) {
        when (navigationState) {
            is NavigationState.NavigateToGame -> onNavigateToGame(navigationState.payload)
            null -> Unit
        }

        navigationState?.let { onNavigationHandled() }
    }
}

@ThemePreviews
@Composable
private fun ContentPreview() {
    QuizApplicationTheme {
        QuizBackground {
            CorrectContent(
                availableMode = AvailableMode.GAME_BUTTON,
                icon = R.drawable.ic_thumb_up_48,
                showIconBackground = true,
                isStartButtonEnabled = true,
                onStartButtonClicked = {},
            )
        }
    }
}
