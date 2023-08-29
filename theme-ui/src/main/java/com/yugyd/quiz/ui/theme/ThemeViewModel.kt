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

import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.LoadAdError
import com.yugyd.quiz.commonui.base.BaseViewModel
import com.yugyd.quiz.commonui.model.mode.ModeUiMapper
import com.yugyd.quiz.commonui.model.mode.ModeUiModel
import com.yugyd.quiz.core.ContentProvider
import com.yugyd.quiz.core.Logger
import com.yugyd.quiz.core.runCatch
import com.yugyd.quiz.domain.api.model.payload.GamePayload
import com.yugyd.quiz.domain.api.payload.SectionPayload
import com.yugyd.quiz.domain.controller.RecordController
import com.yugyd.quiz.domain.options.OptionsInteractor
import com.yugyd.quiz.domain.progress.RecordInteractor
import com.yugyd.quiz.domain.theme.ThemeInteractor
import com.yugyd.quiz.featuretoggle.domain.FeatureManager
import com.yugyd.quiz.featuretoggle.domain.RemoteConfigRepository
import com.yugyd.quiz.featuretoggle.domain.model.FeatureToggle
import com.yugyd.quiz.ui.theme.ThemeView.Action
import com.yugyd.quiz.ui.theme.ThemeView.State
import com.yugyd.quiz.ui.theme.ThemeView.State.NavigationState
import com.yugyd.quiz.ui.theme.model.RewardDialogUiMapper
import com.yugyd.quiz.ui.theme.model.RewardDialogUiModel
import com.yugyd.quiz.ui.theme.model.RewardType
import com.yugyd.quiz.ui.theme.model.ThemeUiMapper
import com.yugyd.quiz.ui.theme.model.ThemeUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themeInteractor: ThemeInteractor,
    private val recordInteractor: RecordInteractor,
    private val remoteConfigRepository: RemoteConfigRepository,
    private val optionsInteractor: OptionsInteractor,
    private val contentProvider: ContentProvider,
    private val recordController: RecordController,
    private val modeMapper: ModeUiMapper,
    private val themeMapper: ThemeUiMapper,
    private val rewardDialogUiMapper: RewardDialogUiMapper,
    private val featureManager: FeatureManager,
    logger: Logger,
) : BaseViewModel<State, Action>(logger, State(isLoading = true)),
    RecordController.Listener {

    init {
        recordController.subscribe(this)
        viewModelScope.launch {
            val isAdFeatureEnabled = featureManager.isFeatureEnabled(FeatureToggle.AD)
            val isTelegramFeatureEnabled = featureManager.isFeatureEnabled(FeatureToggle.TELEGRAM)
            loadThemes(
                isAdFeatureEnabled = isAdFeatureEnabled,
                isTelegramFeatureEnabled = isTelegramFeatureEnabled
            )
            loadAd(isAdFeatureEnabled)
        }
    }

    override fun onCleared() {
        recordController.unsubscribe(this)
        super.onCleared()
    }

    override fun onRecordUpdate() {
        loadThemes(
            isAdFeatureEnabled = screenState.isAdFeatureEnabled,
            isTelegramFeatureEnabled = screenState.isTelegramFeatureEnabled
        )
    }

    override fun handleAction(action: Action) = when (action) {
        is Action.OnInfoClicked -> onInfoClicked(action.model)
        is Action.OnModeChanged -> onModeChanged(action.mode)
        Action.OnNegativeRewardDialogClicked -> onNegativeRewardDialogClicked()
        is Action.OnPositiveResetDialogClicked -> onPositiveResetDialogClicked(action.ignored)
        is Action.OnPositiveRewardDialogClicked -> onPositiveRewardDialogClicked(action.rewardDialog)
        Action.OnRewardAdClosed -> onRewardAdClosed()
        is Action.OnRewardAdFailedToLoad -> onRewardAdFailedToLoad(action.adError)
        Action.OnRewardAdNotToLoad -> onRewardAdNotToLoad()
        is Action.OnStartClicked -> onStartClicked(action.model)
        is Action.OnUserEarnedReward -> onUserEarnedReward(action.theme)
        Action.OnSnackbarDismissed -> {
            screenState = screenState.copy(
                showErrorMessage = false,
                showRewardedErrorMessage = false,
            )
        }

        Action.OnInfoDialogDismissed -> {
            screenState = screenState.copy(
                showInfoDialog = false,
                infoDialogTheme = null,
            )
        }

        Action.OnResetDialogDismissed -> {
            screenState = screenState.copy(
                showResetDialog = false,
                resetDialogTheme = null,
            )
        }

        Action.OnRewardDialogDialogDismissed -> {
            screenState = screenState.copy(
                showRewardedDialog = false,
                rewardedDialogUiModel = null,
            )
        }

        Action.OnNavigationHandled -> {
            screenState = screenState.copy(navigationState = null)
        }

        Action.OnTelegramHandled -> {
            screenState = screenState.copy(
                showTelegram = false,
                telegramLink = "",
            )
        }
    }

    private fun onStartClicked(model: ThemeUiModel) = when (screenState.mode) {
        ModeUiModel.TRAIN -> {
            onTrainStartNext(model)
        }

        ModeUiModel.ARCADE, ModeUiModel.MARATHON, ModeUiModel.NONE, ModeUiModel.ERROR -> {
            onNavigateNext(model)
        }
    }

    private fun onTrainStartNext(model: ThemeUiModel) {
        if (recordInteractor.isRecordReset(model.progressPercent)) {
            screenState = screenState.copy(
                showResetDialog = true,
                resetDialogTheme = model,
            )
        } else {
            if (screenState.isAdFeatureEnabled) {
                loadAd(screenState.isAdFeatureEnabled)
                val dialog = rewardDialogUiMapper.mapToAd(model)
                screenState = screenState.copy(
                    showRewardedDialog = true,
                    rewardedDialogUiModel = dialog,
                )
            } else if (
                screenState.isTelegramFeatureEnabled &&
                !optionsInteractor.isTelegramSubscription
            ) {
                viewModelScope.launch {
                    val config = remoteConfigRepository.fetchTelegramConfig() ?: return@launch

                    val dialog = rewardDialogUiMapper.mapToTelegram(config, model)
                    screenState = screenState.copy(
                        showRewardedDialog = true,
                        rewardedDialogUiModel = dialog,
                    )
                }
            } else {
                onNavigateNext(model)
            }
        }
    }

    private fun onNavigateNext(model: ThemeUiModel) = when (screenState.mode) {
        ModeUiModel.ARCADE -> navigateToSections(model.id, model.title)
        ModeUiModel.MARATHON, ModeUiModel.TRAIN -> navigateToGame(model.id, model.record)
        ModeUiModel.NONE, ModeUiModel.ERROR -> Unit
    }

    private fun onInfoClicked(model: ThemeUiModel) {
        viewModelScope.launch {
            screenState = screenState.copy(
                showInfoDialog = true,
                infoDialogTheme = model,
            )
        }
    }

    private fun onModeChanged(mode: ModeUiModel) {
        val mappedMode = modeMapper.mapToDomain(mode)

        if (screenState.domainMode == mappedMode) {
            return
        }

        screenState = screenState.copy(
            domainMode = mappedMode,
            mode = mode,
        )

        loadThemes(
            isAdFeatureEnabled = screenState.isAdFeatureEnabled,
            isTelegramFeatureEnabled = screenState.isTelegramFeatureEnabled
        )
    }

    private fun onPositiveRewardDialogClicked(rewardDialog: RewardDialogUiModel) {
        when (rewardDialog.rewardType) {
            RewardType.Ad -> {
                screenState = screenState.copy(
                    showRewardedAd = true,
                    rewardedAdTheme = rewardDialog.themeUiModel,
                )
            }

            RewardType.Telegram -> {
                viewModelScope.launch {
                    optionsInteractor.isTelegramSubscription = true

                    val link = contentProvider.getTelegramChannel()
                    screenState = screenState.copy(
                        showTelegram = true,
                        telegramLink = link,
                    )
                }
            }
        }
    }

    private fun onNegativeRewardDialogClicked() {
        // TODO Implement logic with ads - https://yudyd.atlassian.net/browse/QUIZ-203
    }

    private fun onPositiveResetDialogClicked(ignored: ThemeUiModel) {
        // TODO Implement logic with ads - https://yudyd.atlassian.net/browse/QUIZ-203
    }

    private fun onUserEarnedReward(theme: ThemeUiModel) {
        onNavigateNext(theme)
    }

    private fun onRewardAdFailedToLoad(adError: LoadAdError? = null) {
        adError?.let {
            log(it.message)
        }
    }

    private fun onRewardAdNotToLoad() {
        screenState = screenState.copy(showRewardedErrorMessage = true)
    }

    private fun onRewardAdClosed() = loadAd(screenState.isAdFeatureEnabled)

    private fun loadThemes(
        isAdFeatureEnabled: Boolean,
        isTelegramFeatureEnabled: Boolean,
    ) {
        viewModelScope.launch {
            runCatch(
                block = {
                    val themes = themeInteractor
                        .getThemes(screenState.domainMode)
                        .map(themeMapper::map)
                    processThemes(
                        items = themes,
                        isAdFeatureEnabled = isAdFeatureEnabled,
                        isTelegramFeatureEnabled = isTelegramFeatureEnabled
                    )
                },
                catch = ::processThemesError
            )
        }
    }

    private fun processThemes(
        items: List<ThemeUiModel>,
        isAdFeatureEnabled: Boolean,
        isTelegramFeatureEnabled: Boolean,
    ) {
        screenState = screenState.copy(
            mode = modeMapper.map(screenState.domainMode),
            items = items,
            isAdFeatureEnabled = isAdFeatureEnabled,
            isTelegramFeatureEnabled = isTelegramFeatureEnabled,
            isWarning = items.isEmpty(),
            isLoading = false
        )
    }

    private fun processThemesError(error: Throwable) {
        screenState = screenState.copy(
            isLoading = false,
            isWarning = true,
            showErrorMessage = true,
        )
        processError(error)
    }

    private fun navigateToSections(themeId: Int, themeTitle: String) {
        val payload = SectionPayload(
            themeId = themeId,
            themeTitle = themeTitle
        )
        screenState = screenState.copy(
            navigationState = NavigationState.NavigateToSection(payload)
        )
    }

    private fun navigateToGame(themeId: Int, record: Int) {
        val payload = GamePayload(
            mode = screenState.domainMode,
            themeId = themeId,
            record = record,
        )
        screenState = screenState.copy(
            navigationState = NavigationState.NavigateToGame(payload)
        )
    }

    private fun loadAd(isAdFeatureEnabled: Boolean) {
        if (isAdFeatureEnabled) {
            screenState = screenState.copy(loadAd = true)
        }
    }
}
