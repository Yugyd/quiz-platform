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

package com.yugyd.quiz.ui.main

import androidx.lifecycle.viewModelScope
import com.yugyd.quiz.commonui.base.BaseViewModel
import com.yugyd.quiz.core.ContentProvider
import com.yugyd.quiz.core.Logger
import com.yugyd.quiz.domain.api.payload.OnboardingPayload
import com.yugyd.quiz.domain.options.OptionsInteractor
import com.yugyd.quiz.domain.update.UpdateInteractor
import com.yugyd.quiz.featuretoggle.domain.FeatureManager
import com.yugyd.quiz.featuretoggle.domain.RemoteConfigRepository
import com.yugyd.quiz.featuretoggle.domain.model.FeatureToggle
import com.yugyd.quiz.ui.main.MainView.Action
import com.yugyd.quiz.ui.main.MainView.State
import com.yugyd.quiz.ui.main.MainView.State.NavigationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val featureManager: FeatureManager,
    private val remoteConfigRepository: RemoteConfigRepository,
    private val contentProvider: ContentProvider,
    logger: Logger,
    private val optionsInteractor: OptionsInteractor,
    private val updateInteractor: UpdateInteractor
) : BaseViewModel<State, Action>(logger, State()) {

    init {
        viewModelScope.launch {
            val isCorrectFeatureEnabled = featureManager.isFeatureEnabled(FeatureToggle.CORRECT)
            val isTelegramFeatureEnabled = featureManager.isFeatureEnabled(FeatureToggle.TELEGRAM)

            screenState = screenState.copy(
                isCorrectFeatureEnabled = isCorrectFeatureEnabled,
                requestPushPermission = true,
            )

            if (optionsInteractor.isTelegramPopupShow() && isTelegramFeatureEnabled) {
                showTelegramOnboarding()
            }

            if (updateInteractor.shouldForceUpdateApp()) {
                screenState = screenState.copy(navigationState = NavigationState.NavigateToUpdate)
            }
        }
    }

    override fun handleAction(action: Action) {
        when (action) {
            Action.OnBackPressed -> onBackPressed()
            Action.OnOnboardingClicked -> onOnboardingClicked()
            is Action.ProcessPushData -> processPushData(action.extras)
            Action.RequestPushPermissionHandled -> {
                screenState = screenState.copy(requestPushPermission = false)
            }

            Action.OnboardingBottomSheetDismissed -> {
                screenState = screenState.copy(
                    showOnboarding = false,
                    onboardingPayload = null,
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
    }

    private fun onOnboardingClicked() {
        viewModelScope.launch {
            val link = contentProvider.getTelegramChannel()
            screenState = screenState.copy(
                showTelegram = true,
                telegramLink = link,
            )
        }
    }

    private fun onBackPressed() {
        screenState = screenState.copy(navigationState = NavigationState.Back)
    }

    private fun processPushData(extras: Map<String, String>) {
        viewModelScope.launch {
            val action = extras["action"]

            if (action == "telegram") {
                showTelegramOnboarding()
            }
        }
    }

    private suspend fun showTelegramOnboarding() {
        val telegramMainPopup = remoteConfigRepository.fetchTelegramConfig()?.mainPopup

        if (telegramMainPopup != null) {
            optionsInteractor.setNextPopupTelegramDate()

            val payload = OnboardingPayload(
                title = telegramMainPopup.title,
                message = telegramMainPopup.message,
                buttonTitle = telegramMainPopup.buttonTitle
            )
            screenState = screenState.copy(
                showOnboarding = true,
                onboardingPayload = payload,
            )
        }
    }
}
