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

package com.yugyd.quiz.update

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.QuizBackground
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme
import com.yugyd.quiz.update.UpdateView.Action
import com.yugyd.quiz.update.UpdateView.State
import com.yugyd.quiz.update.UpdateView.State.NavigationState
import com.yugyd.quiz.update.UpdateView.State.UpdateConfigUiModel
import com.yugyd.quiz.uikit.R as UiKitR

@Composable
internal fun UpdateRoute(
    viewModel: UpdateViewModel = hiltViewModel(),
    navigateToGooglePlay: () -> Unit,
    navigateToBrowser: (String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    UpdateScreen(
        model = state,
        navigateToGooglePlay = navigateToGooglePlay,
        navigateToBrowser = navigateToBrowser,
        onUpdateClicked = {
            viewModel.onAction(Action.OnUpdateClicked)
        },
        onNavigationHandled = {
            viewModel.onAction(Action.OnNavigationHandled)
        }
    )
}

@Composable
internal fun UpdateScreen(
    model: State,
    navigateToGooglePlay: () -> Unit,
    navigateToBrowser: (String) -> Unit,
    onUpdateClicked: () -> Unit,
    onNavigationHandled: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = UiKitR.drawable.ic_auto_awesome_24),
            contentDescription = null,
            modifier = Modifier.size(96.dp),
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colorScheme.onBackground,
            ),
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = model.updateConfig.title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = model.updateConfig.message,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onUpdateClicked,
        ) {
            Text(text = model.updateConfig.buttonTitle)
        }
    }

    NavigationHandler(
        navigationState = model.navigationState,
        onNavigationHandled = onNavigationHandled,
        navigateToGooglePlay = navigateToGooglePlay,
        navigateToBrowser = navigateToBrowser,
    )
}

@Composable
internal fun NavigationHandler(
    navigationState: NavigationState?,
    navigateToGooglePlay: () -> Unit,
    navigateToBrowser: (String) -> Unit,
    onNavigationHandled: () -> Unit,
) {
    LaunchedEffect(key1 = navigationState) {
        when (navigationState) {
            is NavigationState.NavigateToGooglePlay -> {
                if (!navigationState.storeLink.isNullOrEmpty()) {
                    navigateToBrowser(navigationState.storeLink)
                } else {
                    navigateToGooglePlay()

                }
            }

            null -> Unit
        }

        navigationState?.let { onNavigationHandled() }
    }
}

@ThemePreviews
@Composable
private fun UpdateScreenPreview() {
    QuizApplicationTheme {
        QuizBackground {
            UpdateScreen(
                State(
                    updateConfig = UpdateConfigUiModel(
                        buttonTitle = "Button",
                        message = "Message",
                        title = "Title",
                    ),
                    isLoading = false,
                    navigationState = null,
                ),
                onNavigationHandled = {},
                onUpdateClicked = {},
                navigateToGooglePlay = {},
                navigateToBrowser = {},
            )
        }
    }
}
