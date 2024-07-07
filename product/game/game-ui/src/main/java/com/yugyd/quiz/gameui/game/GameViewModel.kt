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
import com.google.android.gms.ads.LoadAdError
import com.yugyd.quiz.commonui.base.BaseViewModel
import com.yugyd.quiz.core.GlobalConfig
import com.yugyd.quiz.core.Logger
import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import com.yugyd.quiz.core.runCatch
import com.yugyd.quiz.domain.api.model.payload.GameEndPayload
import com.yugyd.quiz.domain.game.GameInteractor
import com.yugyd.quiz.domain.game.api.BaseQuestDomainModel
import com.yugyd.quiz.domain.game.api.model.HighlightModel
import com.yugyd.quiz.domain.game.exception.FinishGameException
import com.yugyd.quiz.domain.game.exception.RewardedGameException
import com.yugyd.quiz.domain.game.model.GameState
import com.yugyd.quiz.domain.options.OptionsInteractor
import com.yugyd.quiz.featuretoggle.domain.FeatureManager
import com.yugyd.quiz.featuretoggle.domain.model.FeatureToggle
import com.yugyd.quiz.gameui.game.GameView.Action
import com.yugyd.quiz.gameui.game.GameView.State
import com.yugyd.quiz.gameui.game.GameView.State.NavigationState
import com.yugyd.quiz.gameui.game.mapper.ControlUiMapper
import com.yugyd.quiz.gameui.game.mapper.HighlightUiMapper
import com.yugyd.quiz.gameui.game.model.ControlUiModel
import com.yugyd.quiz.gameui.game.model.RewardedAdStatus
import com.yugyd.quiz.ui.game.api.model.BaseQuestUiModel
import com.yugyd.quiz.ui.game.api.model.HighlightUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class GameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val gameInteractor: GameInteractor,
    private val optionsInteractor: OptionsInteractor,
    private val controlUiMapper: ControlUiMapper,
    private val gameViewModelDelegateHolder: GameViewModelDelegateHolder,
    private val highlightUiMapper: HighlightUiMapper,
    private val featureManager: FeatureManager,
    logger: Logger,
    dispatchersProvider: DispatchersProvider,
) :
    BaseViewModel<State, Action>(
        logger = logger,
        dispatchersProvider = dispatchersProvider,
        initialState = State(
            payload = GamePayloadArgs(savedStateHandle).payload,
        )
    ) {

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

            is Action.OnAnswerTextChanged -> {
                screenState = screenState.copy(
                    manualAnswer = action.userAnswer,
                )
            }
        }
    }

    private fun onProBannerClicked() {
        screenState = screenState.copy(
            navigationState = NavigationState.NavigateToPro,
        )
    }

    private fun onBackPressed() {
        vmScopeErrorHandled.launch {
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
        vmScopeErrorHandled.launch {
            runCatch(
                block = {
                    val answerData = gameInteractor.resultAnswer(
                        quest = screenState.domainQuest!!,
                        index = index,
                        userAnswer = screenState.manualAnswer
                    )
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
        vmScopeErrorHandled.launch {
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

    private fun initData() {
        vmScopeErrorHandled.launch {
            screenState = screenState.copy(
                isLoading = true,
                isWarning = false,
                domainQuest = null,
                quest = null,
                control = ControlUiModel(),
            )

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
                catch = ::processGameError,
            )
        }
    }

    private fun processGameData(
        data: GameState,
        isAdFeatureEnabled: Boolean,
        isProFeatureEnabled: Boolean,
    ) {
        val highlight = HighlightUiModel.Default
        val answerButtonIsEnabled = true

        val quest = data.quest
        screenState = screenState.copy(
            domainQuest = quest,
            manualAnswer = "",
            quest = getNewQuestState(
                domainQuest = quest,
                highlight = highlight,
                answerButtonIsEnabled = answerButtonIsEnabled,
            ),
            control = controlUiMapper.map(data.control),
            isAdFeatureEnabled = isAdFeatureEnabled,
            isProFeatureEnabled = isProFeatureEnabled,
            isWarning = false,
            isLoading = false,
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
        is FinishGameException -> {
            finishGame()
        }

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
                isLoading = false,
                isWarning = true,
                showErrorMessage = true,
                domainQuest = null,
                quest = null,
                control = ControlUiModel(),
            )
        }
    }

    private fun processAnswerData(data: HighlightModel) {
        val highlight = highlightUiMapper.map(data)
        val answerButtonIsEnabled = false

        screenState = screenState.copy(
            quest = getNewQuestState(
                domainQuest = requireNotNull(screenState.domainQuest),
                highlight = highlight,
                answerButtonIsEnabled = answerButtonIsEnabled
            )
        )

        if (isVibration(highlight)) {
            screenState = screenState.copy(startErrorVibration = true)
        }

        blockAnswer()
    }

    private fun getNewQuestState(
        domainQuest: BaseQuestDomainModel,
        highlight: HighlightUiModel,
        answerButtonIsEnabled: Boolean,
    ): BaseQuestUiModel {
        val args = gameViewModelDelegateHolder.getArgs(
            domainQuest = domainQuest,
            userAnswer = screenState.manualAnswer,
            answerButtonIsEnabled = answerButtonIsEnabled,
        )
        return gameViewModelDelegateHolder.getNewQuestState(
            domainQuest = domainQuest,
            highlight = highlight,
            args = args,
        )
    }

    private fun isVibration(highlight: HighlightUiModel) =
        optionsInteractor.isVibration && highlight is HighlightUiModel.False

    private fun blockAnswer() {
        vmScopeErrorHandled.launch {
            delay(optionsInteractor.transition.value.toLong() * 1000)

            processBlockAnswer()
        }
    }

    private fun processBlockAnswer() {
        screenState = screenState.copy(
            quest = getNewQuestState(
                domainQuest = requireNotNull(screenState.domainQuest),
                highlight = HighlightUiModel.Default,
                answerButtonIsEnabled = true,
            ),
        )

        continueGame()
    }

    private fun continueGame() {
        vmScopeErrorHandled.launch {
            runCatch(
                block = {
                    val gameData = gameInteractor.continueGame()
                    processGameData(
                        data = gameData,
                        isAdFeatureEnabled = screenState.isAdFeatureEnabled,
                        isProFeatureEnabled = screenState.isProFeatureEnabled,
                    )
                },
                catch = ::processGameError,
            )
        }
    }

    private fun finishGame() {
        vmScopeErrorHandled.launch {
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
