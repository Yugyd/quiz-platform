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

import com.yugyd.quiz.ad.api.AdErrorDomainModel
import com.yugyd.quiz.commonui.base.BaseViewModel
import com.yugyd.quiz.commonui.model.mode.ModeUiMapper
import com.yugyd.quiz.commonui.model.mode.ModeUiModel
import com.yugyd.quiz.core.AdIdJvmProvider
import com.yugyd.quiz.core.ContentProvider
import com.yugyd.quiz.core.Logger
import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import com.yugyd.quiz.core.runCatch
import com.yugyd.quiz.domain.api.model.payload.GamePayload
import com.yugyd.quiz.domain.api.payload.SectionPayload
import com.yugyd.quiz.domain.content.ContentInteractor
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
import com.yugyd.quiz.ui.theme.ThemeView.State.RewardAdState
import com.yugyd.quiz.ui.theme.model.RewardDialogUiMapper
import com.yugyd.quiz.ui.theme.model.RewardDialogUiModel
import com.yugyd.quiz.ui.theme.model.RewardType
import com.yugyd.quiz.ui.theme.model.ThemeUiMapper
import com.yugyd.quiz.ui.theme.model.ThemeUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ThemeViewModel @Inject constructor(
    private val themeInteractor: ThemeInteractor,
    private val recordInteractor: RecordInteractor,
    private val remoteConfigRepository: RemoteConfigRepository,
    private val optionsInteractor: OptionsInteractor,
    private val contentInteractor: ContentInteractor,
    private val contentProvider: ContentProvider,
    private val recordController: RecordController,
    private val modeMapper: ModeUiMapper,
    private val themeMapper: ThemeUiMapper,
    private val rewardDialogUiMapper: RewardDialogUiMapper,
    private val featureManager: FeatureManager,
    dispatchersProvider: DispatchersProvider,
    private val logger: Logger,
    private val adIdJvmProvider: AdIdJvmProvider,
) :
    BaseViewModel<State, Action>(
        logger = logger,
        dispatchersProvider = dispatchersProvider,
        initialState = State(),
    ),
    RecordController.Listener {

    private var loadDataJob: Job? = null

    init {
        recordController.subscribe(this)

        vmScopeErrorHandled.launch {
            val isAdFeatureEnabled = featureManager.isFeatureEnabled(FeatureToggle.AD) &&
                    featureManager.isFeatureEnabled(FeatureToggle.AD_REWARDED_THEME)
            val isTelegramFeatureEnabled = featureManager.isFeatureEnabled(FeatureToggle.TELEGRAM)

            loadData(
                isAdFeatureEnabled = isAdFeatureEnabled,
                isTelegramFeatureEnabled = isTelegramFeatureEnabled,
            )

            loadAd(isAdFeatureEnabled)
        }
    }

    override fun onCleared() {
        recordController.unsubscribe(this)
        super.onCleared()
    }

    override fun onRecordUpdate() {
        loadData(
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
        is Action.OnRewardAdFailedToShow -> onRewardAdFailedToLoad(action.adError)
        Action.OnRewardAdNotToLoad -> {
            screenState = screenState.copy(rewardAdState = RewardAdState.NOT_LOADED)
        }

        is Action.OnStartClicked -> onStartClicked(action.model)
        is Action.OnUserEarnedReward -> onUserEarnedReward(screenState.rewardedAdTheme)
        Action.OnSnackbarDismissed -> {
            screenState = screenState.copy(
                showErrorMessage = false,
                showRewardedErrorMessage = false,
            )
        }

        Action.OnInfoDialogDismissed -> {
            screenState = screenState.copy(showInfoDialog = null)
        }

        Action.OnResetDialogDismissed -> {
            screenState = screenState.copy(showResetDialog = null)
        }

        Action.OnRewardDialogDialogDismissed -> {
            screenState = screenState.copy(showRewardedDialog = null)
        }

        Action.OnNavigationHandled -> {
            screenState = screenState.copy(navigationState = null)
        }

        Action.OnTelegramHandled -> {
            screenState = screenState.copy(showTelegram = "")
        }

        Action.OnRewardAdLoaded -> {
            screenState = screenState.copy(rewardAdState = RewardAdState.IS_LOADED)
        }
    }

    private fun onStartClicked(model: ThemeUiModel) = when (screenState.mode) {
        ModeUiModel.TRAIN -> {
            onTrainStartNext(model)
        }

        ModeUiModel.ARCADE,
        ModeUiModel.NONE,
        ModeUiModel.ERROR,
        ModeUiModel.FAVORITE,
        ModeUiModel.AI_TASKS -> {
            onNavigateNext(model)
        }
    }

    private fun onTrainStartNext(model: ThemeUiModel) {
        if (recordInteractor.isRecordReset(model.progressPercent)) {
            screenState = screenState.copy(showResetDialog = model)
        } else {
            showRewardDialog(model)
        }
    }

    private fun showRewardDialog(model: ThemeUiModel) {
        when {
            screenState.isAdFeatureEnabled -> showRewardedAdRewardDialog(model)
            isTelegramEnabledAndNotSubscription() -> showTelegramRewardDialog(model)
            else -> onNavigateNext(model)
        }
    }

    private fun showRewardedAdRewardDialog(model: ThemeUiModel) {
        val dialog = rewardDialogUiMapper.mapToAd(model)

        screenState = screenState.copy(showRewardedDialog = dialog)
    }

    private fun isTelegramEnabledAndNotSubscription(): Boolean {
        return screenState.isTelegramFeatureEnabled && !optionsInteractor.isTelegramSubscription
    }

    private fun showTelegramRewardDialog(model: ThemeUiModel) {
        vmScopeErrorHandled.launch {
            val config = remoteConfigRepository.fetchTelegramConfig() ?: return@launch

            val dialog = rewardDialogUiMapper.mapToTelegram(config, model)
            screenState = screenState.copy(showRewardedDialog = dialog)
        }
    }

    private fun onNavigateNext(model: ThemeUiModel) = when (screenState.mode) {
        ModeUiModel.ARCADE -> navigateToSections(model.id, model.title)
        ModeUiModel.TRAIN -> navigateToGame(model.id, model.record)
        ModeUiModel.NONE, ModeUiModel.ERROR, ModeUiModel.FAVORITE, ModeUiModel.AI_TASKS -> Unit
    }

    private fun onInfoClicked(model: ThemeUiModel) {
        screenState = screenState.copy(showInfoDialog = model)
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

        loadData(
            isAdFeatureEnabled = screenState.isAdFeatureEnabled,
            isTelegramFeatureEnabled = screenState.isTelegramFeatureEnabled
        )
    }

    private fun onPositiveRewardDialogClicked(rewardDialog: RewardDialogUiModel) {
        when (rewardDialog.rewardType) {
            RewardType.Ad -> {
                showRewardedAdOrNextIfAdNotLoaded(rewardDialog)
            }

            RewardType.Telegram -> {
                vmScopeErrorHandled.launch {
                    optionsInteractor.isTelegramSubscription = true

                    val link = contentProvider.getTelegramChannel()
                    screenState = screenState.copy(showTelegram = link)
                }
            }
        }
    }

    private fun showRewardedAdOrNextIfAdNotLoaded(rewardDialog: RewardDialogUiModel) {
        if (screenState.rewardAdState != RewardAdState.IS_LOADED) {
            showRewardAdError()
            onNavigateNext(rewardDialog.themeUiModel)
        } else {
            screenState = screenState.copy(
                rewardAdState = RewardAdState.SHOW_AD,
                rewardedAdTheme = rewardDialog.themeUiModel,
            )
        }
    }

    private fun onNegativeRewardDialogClicked() {
        screenState = screenState.copy(showRewardedDialog = null)
    }

    private fun onPositiveResetDialogClicked(ignored: ThemeUiModel) {
        // TODO Implement logic with ads - https://yudyd.atlassian.net/browse/QUIZ-203
    }

    private fun onUserEarnedReward(theme: ThemeUiModel?) {
        if (theme != null) {
            onNavigateNext(theme)
        }
    }

    private fun onRewardAdFailedToLoad(adError: AdErrorDomainModel? = null) {
        adError?.let {
            logger.print(TAG, "Reward ad is failed: $it")
        }
        screenState = screenState.copy(rewardAdState = RewardAdState.NOT_LOADED)
    }

    private fun showRewardAdError() {
        screenState = screenState.copy(showRewardedErrorMessage = true)
    }

    private fun onRewardAdClosed() = loadAd(screenState.isAdFeatureEnabled)

    private fun loadData(
        isAdFeatureEnabled: Boolean,
        isTelegramFeatureEnabled: Boolean,
    ) {
        screenState = screenState.copy(
            items = emptyList(),
            isLoading = true,
            isWarning = false,
        )

        loadDataJob?.cancel()
        loadDataJob = vmScopeErrorHandled.launch {
            contentInteractor
                .subscribeToSelectedContent()
                .catch {
                    processDataError(it)
                }
                .collect {
                    loadThemes(
                        isAdFeatureEnabled = isAdFeatureEnabled,
                        isTelegramFeatureEnabled = isTelegramFeatureEnabled,
                    )
                }
        }
    }

    private fun processDataError(error: Throwable) {
        screenState = screenState.copy(
            items = emptyList(),
            isLoading = false,
            isWarning = true,
            showErrorMessage = true,
        )
        processError(error)
    }

    private suspend fun loadThemes(
        isAdFeatureEnabled: Boolean,
        isTelegramFeatureEnabled: Boolean,
    ) {
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
            catch = ::processDataError,
        )
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
            isWarning = false,
            isLoading = false,
        )
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
            screenState = screenState.copy(
                rewardAdState = RewardAdState.LOAD_AD,
                rewardAdUnitId = adIdJvmProvider.themeRewardedAdId(),
            )
        }
    }

    companion object {
        private const val TAG = "ThemeViewModel"
    }
}
