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

package com.yugyd.quiz.ui.end.progressend

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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import com.yugyd.quiz.domain.api.model.payload.GameEndPayload
import com.yugyd.quiz.ui.end.R
import com.yugyd.quiz.ui.end.progressend.ProgressEndView.Action
import com.yugyd.quiz.ui.end.progressend.ProgressEndView.State
import com.yugyd.quiz.ui.end.progressend.ProgressEndView.State.NavigationState
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.IconWithBackground
import com.yugyd.quiz.uikit.component.QuizBackground
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme

/**
 * @param onNavigateToGameEnd должен быть через replace
 */
@Composable
internal fun ProgressEndRoute(
    viewModel: ProgressEndViewModel = hiltViewModel(),
    onNavigateToGameEnd: (GameEndPayload) -> Unit,
    onNavigateToRate: () -> Unit,
    onBack: () -> Unit,
    onNavigateToTelegram: (String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProgressEndScreen(
        uiState = state,
        onRateButtonClicked = {
            viewModel.onAction(Action.OnRateClicked)
        },
        onSkipButtonClicked = {
            viewModel.onAction(Action.OnSkipClicked)
        },
        onNavigateToTelegram = onNavigateToTelegram,
        onNavigateToGameEnd = onNavigateToGameEnd,
        onNavigateToRate = onNavigateToRate,
        onBack = onBack,
        onTelegramHandled = {
            viewModel.onAction(Action.OnTelegramHandled)
        },
        onNavigationHandled = {
            viewModel.onAction(Action.OnNavigationHandled)
        },
    )
}

@Composable
internal fun ProgressEndScreen(
    uiState: State,
    onRateButtonClicked: () -> Unit,
    onSkipButtonClicked: () -> Unit,
    onNavigateToTelegram: (String) -> Unit,
    onNavigateToGameEnd: (GameEndPayload) -> Unit,
    onNavigateToRate: () -> Unit,
    onBack: () -> Unit,
    onTelegramHandled: () -> Unit,
    onNavigationHandled: () -> Unit,
) {
    LaunchedEffect(key1 = uiState.showTelegram) {
        if (uiState.showTelegram) {
            onNavigateToTelegram(uiState.telegramLink)

            onTelegramHandled()
        }
    }

    ProgressEndContent(
        title = uiState.title,
        message = uiState.message,
        icon = R.drawable.ic_rewarded_ads_48,
        showIconBackground = true,
        actionButtonTitle = uiState.actionButtonTitle,
        onRateButtonClicked = onRateButtonClicked,
        onSkipButtonClicked = onSkipButtonClicked,
    )

    NavigationHandler(
        navigationState = uiState.navigationState,
        onNavigateToGameEnd = onNavigateToGameEnd,
        onNavigateToRate = onNavigateToRate,
        onBack = onBack,
        onNavigationHandled = onNavigationHandled,
    )
}

@Composable
internal fun ProgressEndContent(
    title: String,
    message: String,
    @DrawableRes icon: Int,
    showIconBackground: Boolean,
    actionButtonTitle: String,
    onRateButtonClicked: () -> Unit,
    onSkipButtonClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(all = 16.dp)
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
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = message,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRateButtonClicked,
        ) {
            Text(text = actionButtonTitle)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onSkipButtonClicked,
            colors = ButtonDefaults.filledTonalButtonColors(),
        ) {
            Text(text = stringResource(id = R.string.end_action_skip))
        }
    }
}

@Composable
internal fun NavigationHandler(
    navigationState: NavigationState?,
    onNavigateToGameEnd: (GameEndPayload) -> Unit,
    onNavigateToRate: () -> Unit,
    onBack: () -> Unit,
    onNavigationHandled: () -> Unit,
) {
    LaunchedEffect(key1 = navigationState) {
        when (navigationState) {
            NavigationState.Back -> onBack()
            is NavigationState.NavigateToGameEnd -> onNavigateToGameEnd(navigationState.payload)
            NavigationState.NavigateToGooglePlay -> onNavigateToRate()
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
            ProgressEndContent(
                title = stringResource(id = R.string.end_title_well),
                message = stringResource(id = R.string.end_msg_new_record),
                icon = R.drawable.ic_rewarded_ads_48,
                showIconBackground = true,
                actionButtonTitle = stringResource(id = R.string.end_action_leave_rate),
                onRateButtonClicked = {},
                onSkipButtonClicked = {},
            )
        }
    }
}
