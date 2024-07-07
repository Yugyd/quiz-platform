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

package com.yugyd.quiz.gameui.game

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.yugyd.quiz.commonui.utils.ProgressUtils
import com.yugyd.quiz.core.AdIdProvider
import com.yugyd.quiz.core.ResIdProvider
import com.yugyd.quiz.domain.api.model.payload.GameEndPayload
import com.yugyd.quiz.domain.game.api.exception.QuestTypeException
import com.yugyd.quiz.gameui.R
import com.yugyd.quiz.gameui.game.GameView.Action
import com.yugyd.quiz.gameui.game.GameView.State
import com.yugyd.quiz.gameui.game.GameView.State.AdBannerState
import com.yugyd.quiz.gameui.game.GameView.State.NavigationState
import com.yugyd.quiz.gameui.game.model.ConditionUiModel
import com.yugyd.quiz.gameui.game.model.ControlUiModel
import com.yugyd.quiz.gameui.game.model.GameStateUiModel
import com.yugyd.quiz.ui.enterquest.EnterQuestContent
import com.yugyd.quiz.ui.enterquest.EnterQuestUiModel
import com.yugyd.quiz.ui.game.api.model.BaseQuestUiModel
import com.yugyd.quiz.ui.simplequest.SimpleQuestContent
import com.yugyd.quiz.ui.simplequest.SimpleQuestUiModel
import com.yugyd.quiz.uikit.LoadingContent
import com.yugyd.quiz.uikit.WarningContent
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.QuizBackground
import com.yugyd.quiz.uikit.component.SimpleToolbar
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme
import com.yugyd.quiz.uikit.R as UiKitR

/**
 * TODO Implement logic with ads - https://yudyd.atlassian.net/browse/QUIZ-203
 */
@Composable
internal fun GameRoute(
    viewModel: GameViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    adIdProvider: AdIdProvider,
    resIdProvider: ResIdProvider,
    onNavigateToProOnboarding: () -> Unit,
    onNavigateToProgressEnd: (GameEndPayload) -> Unit,
    onNavigateToGameEnd: (GameEndPayload) -> Unit,
    onBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    GameScreen(
        uiState = state,
        snackbarHostState = snackbarHostState,
        bannerAdUnitId = stringResource(id = adIdProvider.idAdBannerGame()),
        proMessage = stringResource(id = resIdProvider.msgProAdBanner()),
        onBackPressed = {
            viewModel.onAction(Action.OnBackPressed)
        },
        onAnswerClicked = {
            viewModel.onAction(Action.OnAnswerClicked(it))
        },
        onErrorDismissState = {
            viewModel.onAction(Action.OnSnackbarDismissed)
        },
        onAdBannerAnimationEnded = {
            viewModel.onAction(Action.OnAdBannerAnimationEnded)
        },
        onDebugAnswerToastDismissed = {
            viewModel.onAction(Action.OnDebugAnswerToastDismissed)
        },
        onScrollToTopAnimationEnded = {
            viewModel.onAction(Action.OnScrollToTopAnimationEnded)
        },
        onPositiveRewardDialogClicked = {
            viewModel.onAction(Action.OnPositiveRewardDialogClicked)
        },
        onNegativeRewardDialogClicked = {
            viewModel.onAction(Action.OnNegativeRewardDialogClicked)
        },
        onRewardDialogDismissed = {
            viewModel.onAction(Action.OnRewardDialogDismissed)
        },
        onErrorVibrationEnded = {
            viewModel.onAction(Action.OnErrorVibrationEnded)
        },
        onProBannerClicked = {
            viewModel.onAction(Action.OnProBannerClicked)
        },
        onNavigateToProOnboarding = onNavigateToProOnboarding,
        onNavigateToProgressEnd = onNavigateToProgressEnd,
        onNavigateToGameEnd = onNavigateToGameEnd,
        onBack = onBack,
        onNavigationHandled = {
            viewModel.onAction(Action.OnNavigationHandled)
        },
        onAnswerTextChanged = {
            viewModel.onAction(Action.OnAnswerTextChanged(it))
        },
    )
}

@Composable
internal fun GameScreen(
    uiState: State,
    snackbarHostState: SnackbarHostState,
    bannerAdUnitId: String,
    proMessage: String,
    onBackPressed: () -> Unit,
    onAnswerClicked: (Int) -> Unit,
    onAnswerTextChanged: (String) -> Unit,
    onErrorDismissState: () -> Unit,
    onAdBannerAnimationEnded: () -> Unit,
    onDebugAnswerToastDismissed: () -> Unit,
    onScrollToTopAnimationEnded: () -> Unit,
    onPositiveRewardDialogClicked: () -> Unit,
    onNegativeRewardDialogClicked: () -> Unit,
    onRewardDialogDismissed: () -> Unit,
    onErrorVibrationEnded: () -> Unit,
    onProBannerClicked: () -> Unit,
    onNavigateToProOnboarding: () -> Unit,
    onNavigateToProgressEnd: (GameEndPayload) -> Unit,
    onNavigateToGameEnd: (GameEndPayload) -> Unit,
    onBack: () -> Unit,
    onNavigationHandled: () -> Unit,
) {
    val errorMessage = stringResource(id = UiKitR.string.error_base)
    LaunchedEffect(key1 = uiState.showErrorMessage) {
        if (uiState.showErrorMessage) {
            snackbarHostState.showSnackbar(message = errorMessage)

            onErrorDismissState()
        }
    }

    if (uiState.showRewardedAd) {
        // TODO Implement logic with ads - https://yudyd.atlassian.net/browse/QUIZ-203
    }

    val rewardedAdNotLoadMessage = stringResource(id = R.string.error_reward_not_loaded)
    LaunchedEffect(key1 = uiState.showRewardedAdNotLoadMessage) {
        if (uiState.showRewardedAdNotLoadMessage) {
            snackbarHostState.showSnackbar(message = rewardedAdNotLoadMessage)

            onErrorDismissState()
        }
    }

    if (uiState.loadAd) {
        // TODO Implement logic with ads - https://yudyd.atlassian.net/browse/QUIZ-203
    }

    LaunchedEffect(
        key1 = uiState.showDebugAnswerToast,
        key2 = uiState.debugTrueAnswer,
    ) {
        if (uiState.showDebugAnswerToast && uiState.debugTrueAnswer != null) {
            snackbarHostState.showSnackbar(message = uiState.debugTrueAnswer)

            onDebugAnswerToastDismissed()
        }
    }

    if (uiState.showRewardedDialog) {
        RewardedDialog(
            onPositiveRewardDialogClicked = onPositiveRewardDialogClicked,
            onNegativeRewardDialogClicked = onNegativeRewardDialogClicked,
            onRewardDialogDismissState = onRewardDialogDismissed,
        )
    }

    if (uiState.startErrorVibration) {
        // TODO Implement vibration - https://yudyd.atlassian.net/browse/QUIZ-205
        onErrorVibrationEnded()
    }

    Column {
        val control = uiState.control
        GameToolbar(
            title = control.countTitle,
            onBackPressed = onBackPressed,
            isConditionIconVisible = control.conditionUiModel == ConditionUiModel.LIFE,
            conditionIcon = control.conditionIcon,
            conditionIconColor = uiState.control.conditionTintColor,
        )

        when {
            uiState.isLoading -> {
                LoadingContent()
            }

            uiState.isWarning -> {
                WarningContent()
            }

            uiState.quest != null -> {
                GameContent(
                    control = control,
                    quest = uiState.quest,
                    manualAnswer = uiState.manualAnswer,
                    scrollToTop = uiState.scrollToTopAnimation,
                    bannerAdUnitId = bannerAdUnitId,
                    adBannerState = uiState.adBannerState,
                    proMessage = proMessage,
                    isAdContainerVisible = uiState.isAdFeatureEnabled && uiState.isProFeatureEnabled,
                    isAdFeatureEnabled = uiState.isAdFeatureEnabled,
                    onAnswerClicked = onAnswerClicked,
                    onScrollToTopAnimationEnded = onScrollToTopAnimationEnded,
                    onProBannerClicked = onProBannerClicked,
                    onAnswerTextChanged = onAnswerTextChanged,
                )
            }
        }
    }

    NavigationHandler(
        navigationState = uiState.navigationState,
        onNavigateToProOnboarding = onNavigateToProOnboarding,
        onNavigateToProgressEnd = onNavigateToProgressEnd,
        onNavigateToGameEnd = onNavigateToGameEnd,
        onBack = onBack,
        onNavigationHandled = onNavigationHandled,
    )
}

@Composable
internal fun GameToolbar(
    title: String,
    onBackPressed: () -> Unit,
    isConditionIconVisible: Boolean,
    @DrawableRes conditionIcon: Int? = null,
    conditionIconColor: Color? = null,
) {
    val finalRightIcon = if (isConditionIconVisible) {
        conditionIcon
    } else {
        null
    }

    SimpleToolbar(
        title = title,
        onBackPressed = onBackPressed,
        rightIcon = finalRightIcon,
        rightIconColor = conditionIconColor,
        onRightIconClicked = {},
    )
}

@Composable
internal fun GameContent(
    control: ControlUiModel,
    quest: BaseQuestUiModel,
    manualAnswer: String,
    scrollToTop: Boolean,
    bannerAdUnitId: String,
    adBannerState: AdBannerState,
    proMessage: String,
    isAdContainerVisible: Boolean,
    isAdFeatureEnabled: Boolean,
    onAnswerClicked: (Int) -> Unit,
    onScrollToTopAnimationEnded: () -> Unit,
    onProBannerClicked: () -> Unit,
    onAnswerTextChanged: (String) -> Unit,
) {
    Column {
        val animatedProgress by animateFloatAsState(
            targetValue = ProgressUtils.toFloatPercent(control.progressPercent),
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
            label = "ProgressFloatAnimation",
        )
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxWidth(),
            color = control.progressColor,
        )

        val scrollState = rememberScrollState()
        LaunchedEffect(key1 = scrollToTop) {
            if (scrollToTop) {
                scrollState.animateScrollTo(value = 0)

                onScrollToTopAnimationEnded()
            }
        }

        Column(
            modifier = Modifier
                .weight(weight = 1F)
                .verticalScroll(scrollState)
                .padding(vertical = 16.dp),
        ) {
            when (quest) {
                is SimpleQuestUiModel -> {
                    SimpleQuestContent(
                        quest = quest,
                        onAnswerClicked = onAnswerClicked,
                    )
                }

                is EnterQuestUiModel -> {
                    EnterQuestContent(
                        quest = quest,
                        manualAnswer = manualAnswer,
                        onAnswerHandler = {
                            onAnswerClicked(-1)
                        },
                        onAnswerTextChanged = onAnswerTextChanged,
                    )
                }

                else -> throw QuestTypeException("Unknown quest type: $quest")
            }
        }

        if (isAdContainerVisible) {
            AdContainer(
                proMessage = proMessage,
                bannerAdUnitId = bannerAdUnitId,
                adBannerState = adBannerState,
                isAdFeatureEnabled = isAdFeatureEnabled,
                onProBannerClicked = onProBannerClicked,
            )
        }
    }
}

@Composable
internal fun RewardedDialog(
    onPositiveRewardDialogClicked: () -> Unit,
    onNegativeRewardDialogClicked: () -> Unit,
    onRewardDialogDismissState: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            onRewardDialogDismissState()
        },
        title = {
            Text(text = stringResource(id = R.string.title_add_life))
        },
        text = {
            Text(text = stringResource(id = R.string.msg_add_life))
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onPositiveRewardDialogClicked()
                    onRewardDialogDismissState()
                }
            ) {
                Text(text = stringResource(id = R.string.action_watch))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onNegativeRewardDialogClicked()
                    onRewardDialogDismissState()
                }
            ) {
                Text(text = stringResource(id = R.string.action_next))
            }
        }
    )
}

@Composable
internal fun AdContainer(
    proMessage: String,
    bannerAdUnitId: String,
    adBannerState: AdBannerState,
    isAdFeatureEnabled: Boolean,
    onProBannerClicked: () -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 50.dp)
                .run {
                    when (adBannerState) {
                        AdBannerState.AD -> {
                            this
                        }

                        AdBannerState.PROMO, AdBannerState.LOADING -> {
                            clickable {
                                onProBannerClicked()
                            }
                        }
                    }
                },
            contentAlignment = Alignment.Center,
        ) {
            when (adBannerState) {
                AdBannerState.LOADING -> Unit

                AdBannerState.AD -> {
                    if (isAdFeatureEnabled) {
                        AndroidView(
                            factory = { context ->
                                AdView(context).apply {
                                    setAdSize(AdSize.SMART_BANNER)
                                    adUnitId = bannerAdUnitId
                                    loadAd(AdRequest.Builder().build())
                                }
                            }
                        )
                    } else {
                        PromoContent(proMessage = proMessage)
                    }
                }

                AdBannerState.PROMO -> {
                    PromoContent(proMessage = proMessage)
                }
            }
        }
    }
}

@Composable
internal fun PromoContent(proMessage: String) {
    Text(
        text = proMessage,
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
internal fun NavigationHandler(
    navigationState: NavigationState?,
    onNavigateToProOnboarding: () -> Unit,
    onNavigateToProgressEnd: (GameEndPayload) -> Unit,
    onNavigateToGameEnd: (GameEndPayload) -> Unit,
    onBack: () -> Unit,
    onNavigationHandled: () -> Unit,
) {
    LaunchedEffect(key1 = navigationState) {
        when (navigationState) {
            NavigationState.Back -> onBack()
            is NavigationState.NavigateToGameEnd -> onNavigateToGameEnd(navigationState.payload)
            NavigationState.NavigateToPro -> onNavigateToProOnboarding()
            is NavigationState.NavigateToProgressEnd -> {
                onNavigateToProgressEnd(navigationState.payload)
            }

            null -> Unit
        }

        navigationState?.let { onNavigationHandled() }
    }
}

@ThemePreviews
@Composable
private fun ContentPreview(
    @PreviewParameter(GamePreviewParameterProvider::class) gameData: GameStateUiModel,
) {
    QuizApplicationTheme {
        QuizBackground {
            GameContent(
                control = gameData.control,
                quest = gameData.quest,
                manualAnswer = "Manual",
                scrollToTop = false,
                bannerAdUnitId = "ad_unit",
                adBannerState = AdBannerState.PROMO,
                proMessage = "Pro message",
                isAdContainerVisible = true,
                isAdFeatureEnabled = true,
                onAnswerClicked = {},
                onScrollToTopAnimationEnded = {},
                onProBannerClicked = {},
                onAnswerTextChanged = {},
            )
        }
    }
}
