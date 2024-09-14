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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yugyd.quiz.ad.api.AdErrorDomainModel
import com.yugyd.quiz.ad.rewarded.RewardDomainModel
import com.yugyd.quiz.commonui.model.mode.ModeUiModel
import com.yugyd.quiz.domain.api.model.payload.GamePayload
import com.yugyd.quiz.domain.api.payload.SectionPayload
import com.yugyd.quiz.ui.theme.ThemeView.Action
import com.yugyd.quiz.ui.theme.ThemeView.State
import com.yugyd.quiz.ui.theme.ThemeView.State.NavigationState
import com.yugyd.quiz.ui.theme.model.RewardDialogUiModel
import com.yugyd.quiz.ui.theme.model.ThemeUiModel
import com.yugyd.quiz.uikit.LoadingContent
import com.yugyd.quiz.uikit.WarningContent
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.QuizBackground
import com.yugyd.quiz.uikit.component.RootToolbar
import com.yugyd.quiz.uikit.extension.getText
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme
import com.yugyd.quiz.uikit.theme.app_color_positive
import com.yugyd.quiz.uikit.R as UiKitR

@Composable
internal fun ThemeRoute(
    viewModel: ThemeViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    onNavigateToGame: (GamePayload) -> Unit,
    onNavigateToSection: (SectionPayload) -> Unit,
    onNavigateToTelegram: (String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ThemeScreen(
        uiState = state,
        snackbarHostState = snackbarHostState,
        onStartClicked = {
            viewModel.onAction(Action.OnStartClicked(it))
        },
        onInfoClicked = {
            viewModel.onAction(Action.OnInfoClicked(it))
        },
        onTabChanged = {
            viewModel.onAction(Action.OnModeChanged(it))
        },
        onErrorDismissState = {
            viewModel.onAction(Action.OnSnackbarDismissed)
        },
        onInfoDialogDismissState = {
            viewModel.onAction(Action.OnInfoDialogDismissed)
        },
        onPositiveResetDialogClicked = {
            viewModel.onAction(Action.OnPositiveResetDialogClicked(it))
        },
        onResetDialogDismissState = {
            viewModel.onAction(Action.OnResetDialogDismissed)
        },
        onPositiveRewardDialogClicked = {
            viewModel.onAction(Action.OnPositiveRewardDialogClicked(it))
        },
        onNegativeRewardDialogClicked = {
            viewModel.onAction(Action.OnNegativeRewardDialogClicked)
        },
        onRewardDialogDismissState = {
            viewModel.onAction(Action.OnRewardDialogDialogDismissed)
        },
        onNavigateToGame = onNavigateToGame,
        onNavigateToSection = onNavigateToSection,
        onNavigationHandled = {
            viewModel.onAction(Action.OnNavigationHandled)
        },
        onTelegramHandled = {
            viewModel.onAction(Action.OnTelegramHandled)
        },
        onNavigateToTelegram = onNavigateToTelegram,
        onReward = {
            viewModel.onAction(Action.OnUserEarnedReward)
        },
        onRewardAdLoaded = {
            viewModel.onAction(Action.OnRewardAdLoaded)
        },
        onRewardAdFailedToLoad = {
            viewModel.onAction(Action.OnRewardAdFailedToLoad(it))
        },
        onRewardAdDismissed = {
            viewModel.onAction(Action.OnRewardAdClosed)
        },
        onRewardAdFailedToShow = {
            viewModel.onAction(Action.OnRewardAdFailedToShow(it))
        },
    )
}

@Composable
internal fun ThemeScreen(
    uiState: State,
    snackbarHostState: SnackbarHostState,
    onStartClicked: (ThemeUiModel) -> Unit,
    onInfoClicked: (ThemeUiModel) -> Unit,
    onTabChanged: (ModeUiModel) -> Unit,
    onErrorDismissState: () -> Unit,
    onInfoDialogDismissState: () -> Unit,
    onPositiveResetDialogClicked: (ThemeUiModel) -> Unit,
    onResetDialogDismissState: () -> Unit,
    onNavigateToGame: (GamePayload) -> Unit,
    onNavigateToSection: (SectionPayload) -> Unit,
    onNavigationHandled: () -> Unit,
    onTelegramHandled: () -> Unit,
    onNavigateToTelegram: (String) -> Unit,
    // Reward ad
    onPositiveRewardDialogClicked: (RewardDialogUiModel) -> Unit,
    onNegativeRewardDialogClicked: () -> Unit,
    onRewardDialogDismissState: () -> Unit,
    onRewardAdLoaded: () -> Unit,
    onRewardAdFailedToLoad: (AdErrorDomainModel) -> Unit,
    onReward: (RewardDomainModel) -> Unit,
    onRewardAdDismissed: () -> Unit,
    onRewardAdFailedToShow: (AdErrorDomainModel) -> Unit,
) {
    val errorMessage = stringResource(id = UiKitR.string.error_base)
    LaunchedEffect(key1 = uiState.showErrorMessage) {
        if (uiState.showErrorMessage) {
            snackbarHostState.showSnackbar(message = errorMessage)

            onErrorDismissState()
        }
    }

    val rewardedErrorMessage = stringResource(id = R.string.error_rewarded_ad)
    LaunchedEffect(key1 = uiState.showRewardedErrorMessage) {
        if (uiState.showRewardedErrorMessage) {
            snackbarHostState.showSnackbar(message = rewardedErrorMessage)

            onErrorDismissState()
        }
    }

    if (uiState.showInfoDialog != null) {
        InfoDialog(
            model = uiState.showInfoDialog,
            onStartClicked = onStartClicked,
            onInfoDialogDismissState = onInfoDialogDismissState,
        )
    }

    RewardedAd(
        adState = uiState.rewardAdState,
        adUnitId = uiState.rewardAdUnitId?.getText(),
        onAdLoaded = onRewardAdLoaded,
        onAdFailedToLoad = onRewardAdFailedToLoad,
        onAdFailedToShow = onRewardAdFailedToShow,
        onAdDismissed = onRewardAdDismissed,
        onReward = onReward,
    )

    if (uiState.showRewardedDialog != null) {
        RewardedDialog(
            model = uiState.showRewardedDialog,
            onPositiveRewardDialogClicked = onPositiveRewardDialogClicked,
            onNegativeRewardDialogClicked = onNegativeRewardDialogClicked,
            onRewardDialogDismissState = onRewardDialogDismissState,
        )
    }

    if (uiState.showResetDialog != null) {
        ResetDialog(
            model = uiState.showResetDialog,
            onPositiveResetDialogClicked = onPositiveResetDialogClicked,
            onResetDialogDismissState = onResetDialogDismissState,
        )
    }

    LaunchedEffect(key1 = uiState.showTelegram) {
        if (uiState.showTelegram.isNotEmpty()) {
            onNavigateToTelegram(uiState.showTelegram)

            onTelegramHandled()
        }
    }

    Column {
        RootToolbar(
            title = stringResource(id = UiKitR.string.title_theme),
        )

        when {
            uiState.isLoading -> {
                LoadingContent()
            }

            uiState.isWarning -> {
                WarningContent()
            }

            else -> {
                ThemeContent(
                    modes = uiState.modes,
                    selectedMode = uiState.mode,
                    items = uiState.items,
                    onTabChanged = onTabChanged,
                    onStartClicked = onStartClicked,
                    onInfoClicked = onInfoClicked,
                )
            }
        }

        NavigationHandler(
            navigationState = uiState.navigationState,
            onNavigateToGame = onNavigateToGame,
            onNavigateToSection = onNavigateToSection,
            onNavigationHandled = onNavigationHandled,
        )
    }
}

@Composable
internal fun ModeTabs(
    modes: List<ModeUiModel>,
    selectedMode: ModeUiModel,
    onTabChanged: (ModeUiModel) -> Unit,
) {
    val titles = modes.map {
        stringResource(id = it.title)
    }
    val tabIndex = modes.indexOf(selectedMode)
    TabRow(selectedTabIndex = tabIndex) {
        titles.forEachIndexed { index, title ->
            Tab(
                selected = tabIndex == index,
                onClick = {
                    val newSelectedMode = modes[index]
                    onTabChanged(newSelectedMode)
                },
                text = {
                    Text(
                        text = title,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    }
}

@Composable
internal fun ThemeContent(
    modes: List<ModeUiModel>,
    selectedMode: ModeUiModel,
    items: List<ThemeUiModel>,
    onTabChanged: (ModeUiModel) -> Unit,
    onStartClicked: (ThemeUiModel) -> Unit,
    onInfoClicked: (ThemeUiModel) -> Unit,
) {
    Column {
        ModeTabs(
            modes = modes,
            selectedMode = selectedMode,
            onTabChanged = onTabChanged,
        )

        LazyColumn(
            contentPadding = PaddingValues(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = items, key = ThemeUiModel::id
            ) {
                ThemeItem(
                    model = it,
                    onStartClicked = onStartClicked,
                    onInfoClicked = onInfoClicked,
                )
            }
        }
    }
}

@Composable
internal fun NavigationHandler(
    navigationState: NavigationState?,
    onNavigateToGame: (GamePayload) -> Unit,
    onNavigateToSection: (SectionPayload) -> Unit,
    onNavigationHandled: () -> Unit,
) {
    LaunchedEffect(key1 = navigationState) {
        when (navigationState) {
            is NavigationState.NavigateToGame -> onNavigateToGame(navigationState.payload)
            is NavigationState.NavigateToSection -> onNavigateToSection(navigationState.payload)
            null -> Unit
        }

        navigationState?.let { onNavigationHandled() }
    }
}

@ThemePreviews
@Composable
private fun ContentPreview() {
    val themes = buildList {
        repeat(5) {
            add(
                ThemeUiModel(
                    id = it,
                    title = "Title $it",
                    subtitle = "Subtitle $it",
                    imageUri = Uri.EMPTY,
                    progressPercent = 40,
                    record = 40,
                    progressColor = app_color_positive,
                )
            )
        }
    }
    QuizApplicationTheme {
        QuizBackground {
            ThemeContent(
                modes = listOf(ModeUiModel.ARCADE, ModeUiModel.TRAIN),
                selectedMode = ModeUiModel.TRAIN,
                items = themes,
                onTabChanged = {},
                onStartClicked = {},
                onInfoClicked = {},
            )
        }
    }
}
