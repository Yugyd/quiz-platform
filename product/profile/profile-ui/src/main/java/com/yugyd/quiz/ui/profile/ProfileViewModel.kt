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

package com.yugyd.quiz.ui.profile

import androidx.lifecycle.viewModelScope
import com.yugyd.quiz.commonui.base.BaseViewModel
import com.yugyd.quiz.core.ContentProvider
import com.yugyd.quiz.core.Logger
import com.yugyd.quiz.core.runCatch
import com.yugyd.quiz.domain.api.repository.ContentSource
import com.yugyd.quiz.domain.content.ContentInteractor
import com.yugyd.quiz.domain.controller.TransitionController
import com.yugyd.quiz.domain.options.OptionsInteractor
import com.yugyd.quiz.featuretoggle.domain.FeatureManager
import com.yugyd.quiz.featuretoggle.domain.RemoteConfigRepository
import com.yugyd.quiz.featuretoggle.domain.model.FeatureToggle
import com.yugyd.quiz.featuretoggle.domain.model.telegram.TelegramConfig
import com.yugyd.quiz.ui.profile.ProfileView.Action
import com.yugyd.quiz.ui.profile.ProfileView.State
import com.yugyd.quiz.ui.profile.ProfileView.State.NavigationState
import com.yugyd.quiz.ui.profile.model.ProfileUiMapper
import com.yugyd.quiz.ui.profile.model.ProfileUiModel
import com.yugyd.quiz.ui.profile.model.SwitchItemProfileUiModel
import com.yugyd.quiz.ui.profile.model.TypeProfile.FEEDBACK_SECTION
import com.yugyd.quiz.ui.profile.model.TypeProfile.NONE
import com.yugyd.quiz.ui.profile.model.TypeProfile.OTHER_APPS
import com.yugyd.quiz.ui.profile.model.TypeProfile.PLEASE_US_SECTION
import com.yugyd.quiz.ui.profile.model.TypeProfile.PRIVACY_POLICY
import com.yugyd.quiz.ui.profile.model.TypeProfile.PRO
import com.yugyd.quiz.ui.profile.model.TypeProfile.PURCHASES_SECTION
import com.yugyd.quiz.ui.profile.model.TypeProfile.RATE_APP
import com.yugyd.quiz.ui.profile.model.TypeProfile.REPORT_ERROR
import com.yugyd.quiz.ui.profile.model.TypeProfile.RESTORE_PURCHASE
import com.yugyd.quiz.ui.profile.model.TypeProfile.SELECT_CONTENT
import com.yugyd.quiz.ui.profile.model.TypeProfile.SETTINGS_SECTION
import com.yugyd.quiz.ui.profile.model.TypeProfile.SHARE
import com.yugyd.quiz.ui.profile.model.TypeProfile.SOCIAL_SECTION
import com.yugyd.quiz.ui.profile.model.TypeProfile.SORT_QUEST
import com.yugyd.quiz.ui.profile.model.TypeProfile.TELEGRAM_SOCIAL
import com.yugyd.quiz.ui.profile.model.TypeProfile.TRANSITION
import com.yugyd.quiz.ui.profile.model.TypeProfile.VIBRATION
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val optionsInteractor: OptionsInteractor,
    private val transitionController: TransitionController,
    private val profileUiMapper: ProfileUiMapper,
    private val contentSource: ContentSource,
    private val featureManager: FeatureManager,
    private val remoteConfigRepository: RemoteConfigRepository,
    private val contentProvider: ContentProvider,
    private val contentInteractor: ContentInteractor,
    logger: Logger,
) : BaseViewModel<State, Action>(logger, State(isLoading = true)),
    TransitionController.Listener {

    init {
        transitionController.subscribe(this)
        initData()
    }

    override fun onCleared() {
        transitionController.unsubscribe(this)
        super.onCleared()
    }

    override fun onTransitionChanged() {
        initData()
    }

    override fun handleAction(action: Action) {
        when (action) {
            is Action.OnProfileClicked -> onProfileClicked(action.item)
            is Action.OnProfileItemChecked -> onProfileItemChecked(action.item, action.isChecked)
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

    private fun onProfileClicked(item: ProfileUiModel) = when (item.type) {
        TRANSITION -> {
            navigateToScreen(NavigationState.NavigateToTransition)
        }

        PRO -> {
            navigateToProOnboarding()
        }

        RESTORE_PURCHASE -> {
            restorePurchases()
        }

        RATE_APP -> {
            navigateToScreen(NavigationState.NavigateToGooglePlay)
        }

        SHARE -> {
            navigateToScreen(NavigationState.NavigateToShare)
        }

        OTHER_APPS -> {
            navigateToScreen(NavigationState.NavigateToOtherApps)
        }

        REPORT_ERROR -> {
            navigateToScreen(NavigationState.NavigateToExternalReportError)
        }

        PRIVACY_POLICY -> {
            navigateToScreen(NavigationState.NavigateToPrivacyPolicy)
        }

        SORT_QUEST, VIBRATION -> Unit
        SETTINGS_SECTION, PURCHASES_SECTION, PLEASE_US_SECTION, FEEDBACK_SECTION, SOCIAL_SECTION, NONE -> Unit
        TELEGRAM_SOCIAL -> openTelegram()
        SELECT_CONTENT -> {
            navigateToScreen(NavigationState.NavigateToContents)
        }
    }

    private fun onProfileItemChecked(item: SwitchItemProfileUiModel, isChecked: Boolean) =
        when (item.type) {
            SORT_QUEST -> {
                changeSwitch(item, isChecked) { optionsInteractor.isSortingQuest = isChecked }
            }

            VIBRATION -> {
                changeSwitch(item, isChecked) { optionsInteractor.isVibration = isChecked }
            }

            else -> Unit
        }

    private fun initData() {
        screenState = screenState.copy(isLoading = true)

        viewModelScope.launch {
            runCatch(
                block = {
                    val isProEnabled = featureManager.isFeatureEnabled(FeatureToggle.PRO)
                    val isTelegramEnabled = featureManager.isFeatureEnabled(FeatureToggle.TELEGRAM)
                    val telegramConfig = remoteConfigRepository.fetchTelegramConfig()
                    val contentTitle = contentInteractor.getSelectedContent()?.name

                    processState(
                        isProEnabled = isProEnabled,
                        isTelegramEnabled = isTelegramEnabled,
                        telegramConfig = telegramConfig,
                        contentTitle = contentTitle,
                    )
                },
                catch = ::processDataError
            )
        }
    }

    private fun processState(
        isProEnabled: Boolean,
        isTelegramEnabled: Boolean,
        telegramConfig: TelegramConfig?,
        contentTitle: String?,
    ) {
        screenState = screenState.copy(
            isProFeatureEnabled = isProEnabled,
            items = profileUiMapper.map(
                content = contentSource.getContent(),
                transition = optionsInteractor.transition,
                isSortedQuest = optionsInteractor.isSortingQuest,
                isVibration = optionsInteractor.isVibration,
                isProFeatureEnabled = isProEnabled,
                isTelegramFeatureEnabled = isTelegramEnabled,
                telegramConfig = telegramConfig,
                contentTitle = contentTitle,
            ),
            isWarning = false,
            isLoading = false
        )
    }

    private fun processDataError(error: Throwable) {
        screenState = screenState.copy(
            isLoading = false,
            isWarning = true
        )
        processError(error)
    }

    private fun changeSwitch(
        item: SwitchItemProfileUiModel,
        isChecked: Boolean,
        finallyBlock: () -> Unit
    ) {
        if (item.isChecked == isChecked) {
            return
        }

        val mappedItems = screenState.items.map {
            if (it is SwitchItemProfileUiModel && it.id == item.id) {
                it.copy(isChecked = isChecked)
            } else {
                it
            }
        }
        screenState = screenState.copy(items = mappedItems)

        finallyBlock()
    }

    private fun restorePurchases() = Unit

    private fun navigateToScreen(
        navigationState: NavigationState,
    ) {
        screenState = screenState.copy(navigationState = navigationState)
    }

    private fun navigateToProOnboarding() {
        if (screenState.isProFeatureEnabled) {
            screenState = screenState.copy(
                navigationState = NavigationState.NavigateToProOnboarding
            )
        }
    }

    private fun openTelegram() {
        viewModelScope.launch {
            val link = contentProvider.getTelegramChannel()
            screenState = screenState.copy(
                showTelegram = true,
                telegramLink = link,
            )
        }
    }
}
