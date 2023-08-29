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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.LoadAdError
import com.yugyd.quiz.commonui.base.BaseViewModel
import com.yugyd.quiz.core.GlobalConfig
import com.yugyd.quiz.core.Logger
import com.yugyd.quiz.core.runCatch
import com.yugyd.quiz.domain.api.model.game.ControlModel
import com.yugyd.quiz.domain.api.model.game.HighlightModel
import com.yugyd.quiz.domain.api.model.game.QuestModel
import com.yugyd.quiz.domain.api.model.payload.GameEndPayload
import com.yugyd.quiz.domain.game.GameInteractor
import com.yugyd.quiz.domain.game.exception.FinishGameException
import com.yugyd.quiz.domain.game.exception.RewardedGameException
import com.yugyd.quiz.domain.options.OptionsInteractor
import com.yugyd.quiz.featuretoggle.domain.FeatureManager
import com.yugyd.quiz.featuretoggle.domain.model.FeatureToggle
import com.yugyd.quiz.gameui.game.GameView.Action
import com.yugyd.quiz.gameui.game.GameView.State
import com.yugyd.quiz.gameui.game.GameView.State.NavigationState
import com.yugyd.quiz.gameui.game.mapper.ControlUiMapper
import com.yugyd.quiz.gameui.game.mapper.HighlightUiMapper
import com.yugyd.quiz.gameui.game.mapper.QuestUiMapper
import com.yugyd.quiz.gameui.game.model.HighlightUiModel
import com.yugyd.quiz.gameui.game.model.RewardedAdStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val gameInteractor: GameInteractor,
    private val optionsInteractor: OptionsInteractor,
    private val controlUiMapper: ControlUiMapper,
    private val questUiMapper: QuestUiMapper,
    private val highlightUiMapper: HighlightUiMapper,
    private val featureManager: FeatureManager,
    logger: Logger,
) : BaseViewModel<State, Action>(
    logger = logger,
    initialState = State(
        payload = GamePayloadArgs(savedStateHandle).payload,
        isLoading = true,
    )
) {

    private var debugableAutoTestJob: Job? = null

    init {
        initData()
    }

    override fun handleAction(action: Action) {
        when (action) {
            is Action.OnAnswerClicked -> onAnswerClicked(action.index)
            Action.OnBackPressed -> onBackPressed()
            is Action.OnBannerAdFailedToLoad -> onBannerAdFailedToLoad(action.adError)
            Action.OnBannerAdLoaded -> onBannerAdLoaded()
            Action.OnNegativeRewardDialogClicked -> onNegativeRewardDialogClicked()
            Action.OnPositiveRewardDialogClicked -> onPositiveRewardDialogClicked()
            Action.OnProBannerClicked -> onProBannerClicked()
            Action.OnRewardAdClosed -> onRewardAdClosed()
            is Action.OnRewardAdFailedToLoad -> onRewardAdFailedToLoad(action.adError)
            Action.OnRewardAdLoad -> onRewardAdLoad()
            Action.OnRewardAdNotShowed -> onRewardAdNotShowed()
            Action.OnUserEarnedReward -> onUserEarnedReward()
            Action.OnAutoTestClicked -> onAutoTestClicked()
            Action.OnAutoTestLongClicked -> onAutoTestLongClicked()
            Action.OnAdBannerAnimationEnded -> {
                screenState = screenState.copy(showAdBannerAnimation = false)
            }

            Action.OnDebugAnswerToastDismissed -> {
                screenState = screenState.copy(
                    showDebugAnswerToast = false,
                    debugTrueAnswer = null,
                )
            }

            Action.OnErrorVibrationEnded -> {
                screenState = screenState.copy(startErrorVibration = false)
            }

            Action.OnRewardDialogDismissed -> {
                screenState = screenState.copy(showRewardedDialog = false)
            }

            Action.OnScrollToTopAnimationEnded -> {
                screenState = screenState.copy(scrollToTopAnimation = false)
            }

            Action.OnSnackbarDismissed -> {
                screenState = screenState.copy(
                    showErrorMessage = false,
                    showRewardedAdNotLoadMessage = false,
                )
            }

            Action.OnNavigationHandled -> {
                screenState = screenState.copy(navigationState = null)
            }
        }
    }

    private fun onProBannerClicked() {
        screenState = screenState.copy(
            navigationState = NavigationState.NavigateToPro,
        )
    }

    private fun onBackPressed() {
        viewModelScope.launch {
            runCatch(
                block = {
                    gameInteractor.firstFinishGame()
                },
                catch = ::processError,
                finally = {
                    screenState = screenState.copy(navigationState = NavigationState.Back)
                }
            )
        }
    }

    private fun onAnswerClicked(index: Int) {
        viewModelScope.launch {
            runCatch(
                block = {
                    val answerData = gameInteractor.resultAnswer(screenState.domainQuest, index)
                    processAnswerData(answerData)
                },
                catch = ::processError
            )
        }
    }

    private fun onPositiveRewardDialogClicked() {
        screenState = screenState.copy(showRewardedAd = true)
    }

    private fun onNegativeRewardDialogClicked() = continueGame()

    private fun onUserEarnedReward() {
        viewModelScope.launch {
            runCatch(
                block = {
                    gameInteractor.onUserEarnedReward()
                },
                catch = ::processError
            )
        }
    }

    private fun onRewardAdLoad() {
        screenState = screenState.copy(rewardedAdStatus = RewardedAdStatus.SUCCESS)
    }

    private fun onRewardAdFailedToLoad(adError: LoadAdError? = null) {
        screenState = screenState.copy(rewardedAdStatus = RewardedAdStatus.ERROR)

        adError?.let {
            log(it.message)
        }
    }

    private fun onRewardAdNotShowed() {
        continueGame()
        screenState = screenState.copy(showRewardedAdNotLoadMessage = true)
    }

    private fun onRewardAdClosed() = continueGame()

    private fun onBannerAdLoaded() {
        screenState = screenState.copy(
            adBannerState = State.AdBannerState.AD,
            showAdBannerAnimation = true,
        )
    }

    private fun onBannerAdFailedToLoad(adError: LoadAdError) {
        log(adError.message)
        screenState = screenState.copy(
            adBannerState = State.AdBannerState.PROMO,
            showAdBannerAnimation = true,
        )
    }

    private fun onAutoTestClicked() {
        if (GlobalConfig.DEBUG) {
            debugableAutoTestJob?.cancel()
            debugableAutoTestJob = viewModelScope.launch {
                repeat(Int.MAX_VALUE) {
                    withContext(Dispatchers.Default) {
                        val delayTimeMills = optionsInteractor.transition.value.toLong() * 2000
                        delay(delayTimeMills)
                    }
                    onAction(Action.OnAnswerClicked(screenState.domainQuest.trueAnswerIndex))
                }
            }
        }
    }

    private fun onAutoTestLongClicked() {
        if (GlobalConfig.DEBUG) {
            debugableAutoTestJob?.cancel()
        }
    }

    private fun initData() {
        viewModelScope.launch {
            runCatch(
                block = {
                    val gameData = gameInteractor.startGame(screenState.payload)

                    val isAdFeatureEnabled = featureManager.isFeatureEnabled(FeatureToggle.AD)
                    val isProFeatureEnabled = featureManager.isFeatureEnabled(FeatureToggle.PRO)

                    processGameData(
                        data = gameData,
                        isAdFeatureEnabled = isAdFeatureEnabled,
                        isProFeatureEnabled = isProFeatureEnabled,
                    )

                    if (isAdFeatureEnabled) {
                        screenState = screenState.copy(loadAd = true)
                    }
                },
                catch = ::processGameError
            )
        }
    }

    private fun processGameData(
        data: Pair<QuestModel, ControlModel>,
        isAdFeatureEnabled: Boolean,
        isProFeatureEnabled: Boolean,
    ) {
        val quest = data.first
        screenState = screenState.copy(
            domainQuest = quest,
            quest = questUiMapper.map(quest),
            control = controlUiMapper.map(data.second),
            isAdFeatureEnabled = isAdFeatureEnabled,
            isProFeatureEnabled = isProFeatureEnabled,
            isWarning = false,
            isLoading = false
        )

        if (GlobalConfig.DEBUG) {
            screenState = screenState.copy(
                showDebugAnswerToast = true,
                debugTrueAnswer = quest.trueAnswer,
            )
        }

        screenState = screenState.copy(scrollToTopAnimation = true)
    }

    private fun processGameError(error: Throwable) = when (error) {
        is FinishGameException -> finishGame()
        is RewardedGameException -> {
            if (
                screenState.isAdFeatureEnabled &&
                screenState.rewardedAdStatus == RewardedAdStatus.SUCCESS
            ) {
                screenState = screenState.copy(showRewardedDialog = true)
            } else {
                finishGame()
            }
        }

        else -> {
            processError(error)
            screenState = screenState.copy(
                isWarning = true,
                isLoading = false,
                showErrorMessage = true,
            )
        }
    }

    private fun processAnswerData(data: HighlightModel) {
        val control = screenState.control.copy(
            highlight = highlightUiMapper.map(data),
            answerButtonIsEnabled = false
        )

        screenState = screenState.copy(
            control = control
        )

        if (isVibration(control.highlight)) {
            screenState = screenState.copy(startErrorVibration = true)
        }

        blockAnswer()
    }

    private fun isVibration(highlight: HighlightUiModel) =
        optionsInteractor.isVibration && highlight is HighlightUiModel.False

    private fun blockAnswer() {
        viewModelScope.launch {
            delay(optionsInteractor.transition.value.toLong() * 1000)
            processBlockAnswer()
        }
    }

    private fun processBlockAnswer() {
        val control = screenState.control.copy(
            answerButtonIsEnabled = true,
            highlight = HighlightUiModel.Default
        )

        screenState = screenState.copy(control = control)

        continueGame()
    }

    private fun continueGame() {
        viewModelScope.launch {
            runCatch(
                block = {
                    val gameData = gameInteractor.continueGame()
                    processGameData(
                        data = gameData,
                        isAdFeatureEnabled = screenState.isAdFeatureEnabled,
                        isProFeatureEnabled = screenState.isProFeatureEnabled,
                    )
                },
                catch = ::processGameError
            )
        }
    }

    private fun finishGame() {
        viewModelScope.launch {
            runCatch(
                block = {
                    val payload = gameInteractor.finishGame()
                    processFinishData(payload)
                },
                catch = ::processError,
            )
        }
    }

    private fun processFinishData(payload: GameEndPayload) {
        if (payload.point > payload.oldRecord) {
            screenState = screenState.copy(
                navigationState = NavigationState.NavigateToProgressEnd(payload)
            )
        } else {
            screenState = screenState.copy(
                navigationState = NavigationState.NavigateToGameEnd(payload)
            )
        }
    }
}
