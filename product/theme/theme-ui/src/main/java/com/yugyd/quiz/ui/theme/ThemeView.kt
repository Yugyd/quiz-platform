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

import com.google.android.gms.ads.LoadAdError
import com.yugyd.quiz.commonui.model.mode.ModeUiModel
import com.yugyd.quiz.domain.api.model.Mode
import com.yugyd.quiz.domain.api.model.payload.GamePayload
import com.yugyd.quiz.domain.api.payload.SectionPayload
import com.yugyd.quiz.ui.theme.model.RewardDialogUiModel
import com.yugyd.quiz.ui.theme.model.ThemeUiModel

interface ThemeView {

    data class State(
        val domainMode: Mode = Mode.ARCADE,
        val mode: ModeUiModel = ModeUiModel.ARCADE,
        val modes: List<ModeUiModel> = listOf(
            ModeUiModel.ARCADE,
            ModeUiModel.MARATHON,
            ModeUiModel.TRAIN
        ),
        val items: List<ThemeUiModel> = emptyList(),
        val isAdFeatureEnabled: Boolean = false,
        val isTelegramFeatureEnabled: Boolean = false,
        val isWarning: Boolean = false,
        val isLoading: Boolean = false,
        val showResetDialog: Boolean = false,
        val resetDialogTheme: ThemeUiModel? = null,
        val showRewardedDialog: Boolean = false,
        val rewardedDialogUiModel: RewardDialogUiModel? = null,
        val showInfoDialog: Boolean = false,
        val infoDialogTheme: ThemeUiModel? = null,
        val showRewardedAd: Boolean = false,
        val rewardedAdTheme: ThemeUiModel? = null,
        val showTelegram: Boolean = false,
        val telegramLink: String = "",
        val showRewardedErrorMessage: Boolean = false,
        val showErrorMessage: Boolean = false,
        val loadAd: Boolean = false,
        val navigationState: NavigationState? = null,
    ) {

        sealed interface NavigationState {
            data class NavigateToGame(val payload: GamePayload) : NavigationState
            data class NavigateToSection(val payload: SectionPayload) : NavigationState
        }
    }

    sealed interface Action {
        class OnStartClicked(val model: ThemeUiModel) : Action
        class OnInfoClicked(val model: ThemeUiModel) : Action
        class OnModeChanged(val mode: ModeUiModel) : Action
        class OnPositiveRewardDialogClicked(val rewardDialog: RewardDialogUiModel) : Action
        object OnNegativeRewardDialogClicked : Action
        class OnPositiveResetDialogClicked(val ignored: ThemeUiModel) : Action
        class OnUserEarnedReward(val theme: ThemeUiModel) : Action
        class OnRewardAdFailedToLoad(val adError: LoadAdError? = null) : Action
        object OnRewardAdNotToLoad : Action
        object OnRewardAdClosed : Action
        object OnSnackbarDismissed : Action
        object OnInfoDialogDismissed : Action
        object OnResetDialogDismissed : Action
        object OnRewardDialogDialogDismissed : Action
        object OnTelegramHandled : Action
        object OnNavigationHandled : Action
    }
}
