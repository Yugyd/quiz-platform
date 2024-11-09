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
import com.yugyd.quiz.ad.api.AdErrorDomainModel
import com.yugyd.quiz.commonui.base.BaseViewModel
import com.yugyd.quiz.core.AdIdJvmProvider
import com.yugyd.quiz.core.GlobalConfig
import com.yugyd.quiz.core.Logger
import com.yugyd.quiz.core.ResIdJvmProvider
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
import com.yugyd.quiz.gameui.game.GameView.State.BannerState
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
    private val adIdProvider: AdIdJvmProvider,
    private val resIdProvider: ResIdJvmProvider,
    private val logger: Logger,
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

            Action.OnDebugAnswerToastDismissed -> {
                screenState = screenState.copy(debugTrueAnswer = null)
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
        // TODO Add rewarded AD
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

    private fun onRewardAdFailedToLoad(adError: AdErrorDomainModel? = null) {
        screenState = screenState.copy(rewardedAdStatus = RewardedAdStatus.ERROR)

        adError?.let {
            logger.print(TAG, "Reward ad is failed: $it")
        }
    }

    private fun onRewardAdNotShowed() {
        continueGame()
        screenState = screenState.copy(showRewardedAdNotLoadMessage = true)
    }

    private fun onRewardAdClosed() = continueGame()

    private fun onBannerAdLoaded() {
        screenState = screenState.copy(
            bannerState = BannerState.AdBannerState(
                proMessage = resIdProvider.msgProAdBannerString(),
                bannerAdUnitId = adIdProvider.gameBannerAdId(),
            ),
        )
    }

    private fun onBannerAdFailedToLoad(adError: AdErrorDomainModel) {
        logger.print(TAG, "Banner ad is failed: $adError")

        val bannerState = if (screenState.isProFeatureEnabled) {
            BannerState.PromoBannerState(proMessage = resIdProvider.msgProAdBannerString())
        } else {
            null
        }
        screenState = screenState.copy(bannerState = bannerState)
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

                    val isAdFeatureEnabled = featureManager.isFeatureEnabled(FeatureToggle.AD) &&
                            featureManager.isFeatureEnabled(FeatureToggle.AD_BANNER_GAME)
                    val isProFeatureEnabled = featureManager.isFeatureEnabled(FeatureToggle.PRO)

                    processGameData(
                        data = gameData,
                        isAdFeatureEnabled = isAdFeatureEnabled,
                        isProFeatureEnabled = isProFeatureEnabled,
                    )

                    val bannerState = when {
                        isAdFeatureEnabled -> {
                            BannerState.AdBannerState(
                                proMessage = resIdProvider.msgProAdBannerString(),
                                bannerAdUnitId = adIdProvider.gameBannerAdId(),
                            )
                        }

                        isProFeatureEnabled -> {
                            BannerState.PromoBannerState(
                                proMessage = resIdProvider.msgProAdBannerString(),
                            )
                        }

                        else -> {
                            null
                        }
                    }
                    screenState = screenState.copy(bannerState = bannerState)
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
            val secondDelay = optionsInteractor.transition.value * 1000
            delay(secondDelay.toLong())

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

    companion object {
        private const val TAG = "GameViewModel"
    }
}
