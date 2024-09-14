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
import com.yugyd.quiz.commonui.model.mode.ModeUiModel
import com.yugyd.quiz.core.TextModel
import com.yugyd.quiz.domain.api.model.Mode
import com.yugyd.quiz.domain.api.model.payload.GamePayload
import com.yugyd.quiz.domain.api.payload.SectionPayload
import com.yugyd.quiz.ui.theme.model.RewardDialogUiModel
import com.yugyd.quiz.ui.theme.model.ThemeUiModel

internal interface ThemeView {

    data class State(
        val domainMode: Mode = Mode.ARCADE,
        val mode: ModeUiModel = ModeUiModel.ARCADE,
        val modes: List<ModeUiModel> = listOf(
            ModeUiModel.ARCADE,
            ModeUiModel.TRAIN
        ),
        val items: List<ThemeUiModel> = emptyList(),
        val isAdFeatureEnabled: Boolean = false,
        val isTelegramFeatureEnabled: Boolean = false,
        val isWarning: Boolean = false,
        val isLoading: Boolean = false,
        val showResetDialog: ThemeUiModel? = null,
        val showRewardedDialog: RewardDialogUiModel? = null,
        val rewardedAdTheme: ThemeUiModel? = null,
        val showInfoDialog: ThemeUiModel? = null,
        val showTelegram: String = "",
        val showErrorMessage: Boolean = false,
        val navigationState: NavigationState? = null,
        // Ad
        val rewardAdState: RewardAdState? = null,
        val rewardAdUnitId: TextModel? = null,
        val showRewardedErrorMessage: Boolean = false,
    ) {

        enum class RewardAdState {
            LOAD_AD,
            IS_LOADED,
            NOT_LOADED,
            SHOW_AD,
        }

        sealed interface NavigationState {
            data class NavigateToGame(val payload: GamePayload) : NavigationState
            data class NavigateToSection(val payload: SectionPayload) : NavigationState
        }
    }

    sealed interface Action {
        // Reward Ad
        object OnUserEarnedReward : Action

        class OnRewardAdFailedToLoad(val adError: AdErrorDomainModel? = null) : Action

        class OnRewardAdFailedToShow(val adError: AdErrorDomainModel? = null) : Action

        object OnRewardAdNotToLoad : Action

        object OnRewardAdClosed : Action

        object OnRewardAdLoaded : Action

        // Screen events
        class OnStartClicked(val model: ThemeUiModel) : Action

        class OnInfoClicked(val model: ThemeUiModel) : Action

        class OnModeChanged(val mode: ModeUiModel) : Action

        class OnPositiveRewardDialogClicked(val rewardDialog: RewardDialogUiModel) : Action

        object OnNegativeRewardDialogClicked : Action

        class OnPositiveResetDialogClicked(val ignored: ThemeUiModel) : Action

        object OnSnackbarDismissed : Action

        object OnInfoDialogDismissed : Action

        object OnResetDialogDismissed : Action

        object OnRewardDialogDialogDismissed : Action

        object OnTelegramHandled : Action

        object OnNavigationHandled : Action
    }
}
