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

package com.yugyd.quiz.gameui.progressend

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yugyd.quiz.commonui.base.BaseViewModel
import com.yugyd.quiz.core.ContentProvider
import com.yugyd.quiz.domain.repository.Logger
import com.yugyd.quiz.featuretoggle.domain.FeatureManager
import com.yugyd.quiz.featuretoggle.domain.RemoteConfigRepository
import com.yugyd.quiz.featuretoggle.domain.model.FeatureToggle
import com.yugyd.quiz.gameui.progressend.ProgressEndView.Action
import com.yugyd.quiz.gameui.progressend.ProgressEndView.State
import com.yugyd.quiz.gameui.progressend.ProgressEndView.State.ActionButtonType
import com.yugyd.quiz.gameui.progressend.ProgressEndView.State.NavigationState
import com.yugyd.quiz.gameui.progressend.model.ProgressEndMapper
import com.yugyd.quiz.gameui.shared.EndArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgressEndViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val featureManager: FeatureManager,
    private val repository: RemoteConfigRepository,
    private val contentProvider: ContentProvider,
    private val progressEndMapper: ProgressEndMapper,
    logger: Logger,
) : BaseViewModel<State, Action>(
    logger = logger,
    initialState = State(
        payload = EndArgs(savedStateHandle).payload,
    )
) {

    init {
        viewModelScope.launch {
            val isTelegramFeatureEnabled = featureManager.isFeatureEnabled(FeatureToggle.TELEGRAM)

            screenState = if (isTelegramFeatureEnabled) {
                val config = repository.fetchTelegramConfig()
                progressEndMapper.map(screenState, config)
            } else {
                progressEndMapper.map(screenState, null)
            }
        }
    }

    override fun handleAction(action: Action) {
        when (action) {
            Action.OnRateClicked -> onRateClicked()
            Action.OnSkipClicked -> onSkipClicked()
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
    }

    private fun onRateClicked() {
        screenState = screenState.copy(isRated = true)

        when (screenState.actionButtonType) {
            ActionButtonType.RATE -> {
                screenState = screenState.copy(
                    navigationState = NavigationState.NavigateToGooglePlay,
                )
            }

            ActionButtonType.TELEGRAM -> {
                viewModelScope.launch {
                    val link = contentProvider.getTelegramChannel()
                    screenState = screenState.copy(
                        showTelegram = true,
                        telegramLink = link,
                    )
                }
            }
        }
    }

    private fun onSkipClicked() {
        val gameEndPayload = screenState.payload.copy(
            isBlockedInterstitial = screenState.isRated
        )
        screenState = screenState.copy(
            navigationState = NavigationState.NavigateToGameEnd(gameEndPayload)
        )
    }
}
