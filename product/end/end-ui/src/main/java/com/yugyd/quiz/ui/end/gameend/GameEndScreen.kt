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

package com.yugyd.quiz.ui.end.gameend

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yugyd.quiz.commonui.utils.ProgressUtils
import com.yugyd.quiz.domain.api.payload.ErrorListPayload
import com.yugyd.quiz.ui.end.R
import com.yugyd.quiz.ui.end.gameend.GameEndView.Action
import com.yugyd.quiz.ui.end.gameend.GameEndView.State
import com.yugyd.quiz.ui.end.gameend.GameEndView.State.NavigationState
import com.yugyd.quiz.uikit.LoadingContent
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.QuizBackground
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme
import com.yugyd.quiz.uikit.theme.app_color_positive
import com.yugyd.quiz.uikit.theme.progressIndicatorHeight

/**
 * TODO Implement logic with ads - https://yudyd.atlassian.net/browse/QUIZ-203
 */
@Composable
fun GameEndRoute(
    viewModel: GameEndViewModel = hiltViewModel(),
    onNavigateToErrors: (ErrorListPayload) -> Unit,
    onBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    GameEndScreen(
        uiState = state,
        onNewGameButtonClicked = {
            viewModel.onAction(Action.OnNewGameClicked)
        },
        onShowErrorsButtonClicked = {
            viewModel.onAction(Action.OnShowErrorsClicked)
        },
        onNavigateToErrors = onNavigateToErrors,
        onBack = onBack,
        onNavigationHandled = {
            viewModel.onAction(Action.OnNavigationHandled)
        },
    )
}

@Composable
internal fun GameEndScreen(
    uiState: State,
    onNewGameButtonClicked: () -> Unit,
    onShowErrorsButtonClicked: () -> Unit,
    onNavigateToErrors: (ErrorListPayload) -> Unit,
    onBack: () -> Unit,
    onNavigationHandled: () -> Unit,
) {
    when {
        uiState.isLoading -> {
            LoadingContent()
        }

        else -> {
            GameEndContent(
                themeTitle = uiState.themeTitle,
                progress = uiState.progress,
                progressColor = uiState.progressColor,
                progressTitle = uiState.progressTitle,
                showErrorIsVisible = uiState.showErrorIsVisible,
                onNewGameButtonClicked = onNewGameButtonClicked,
                onShowErrorsButtonClicked = onShowErrorsButtonClicked,
            )
        }
    }

    NavigationHandler(
        navigationState = uiState.navigationState,
        onNavigateToErrorList = onNavigateToErrors,
        onBack = onBack,
        onNavigationHandled = onNavigationHandled,
    )
}

@Composable
internal fun GameEndContent(
    themeTitle: String,
    progress: Int,
    progressColor: Color,
    progressTitle: String,
    showErrorIsVisible: Boolean,
    onNewGameButtonClicked: () -> Unit,
    onShowErrorsButtonClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(all = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = themeTitle,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.height(16.dp))

        val animatedProgress by animateFloatAsState(
            targetValue = ProgressUtils.toFloatPercent(progress),
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        )
        LinearProgressIndicator(
            progress = animatedProgress,
            modifier = Modifier
                .height(height = progressIndicatorHeight)
                .defaultMinSize(minWidth = 100.dp),
            color = progressColor,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = progressTitle,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNewGameButtonClicked,
        ) {
            Text(text = stringResource(id = R.string.action_new_game))
        }

        if (showErrorIsVisible) {
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onShowErrorsButtonClicked,
                colors = ButtonDefaults.filledTonalButtonColors(),
            ) {
                Text(text = stringResource(id = R.string.action_show_error))
            }
        }
    }
}

@Composable
internal fun NavigationHandler(
    navigationState: NavigationState?,
    onNavigateToErrorList: (ErrorListPayload) -> Unit,
    onBack: () -> Unit,
    onNavigationHandled: () -> Unit,
) {
    LaunchedEffect(key1 = navigationState) {
        when (navigationState) {
            NavigationState.Back -> onBack()
            is NavigationState.NavigateToErrorList -> onNavigateToErrorList(navigationState.payload)
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
            GameEndContent(
                themeTitle = "Theme title",
                progress = 90,
                progressTitle = "90 of 100",
                progressColor = app_color_positive,
                showErrorIsVisible = true,
                onNewGameButtonClicked = {},
                onShowErrorsButtonClicked = {},
            )
        }
    }
}
