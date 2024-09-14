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

import com.yugyd.quiz.ad.api.AdErrorDomainModel
import com.yugyd.quiz.core.TextModel
import com.yugyd.quiz.domain.api.model.payload.GameEndPayload
import com.yugyd.quiz.domain.api.model.payload.GamePayload
import com.yugyd.quiz.domain.game.api.BaseQuestDomainModel
import com.yugyd.quiz.gameui.game.model.ControlUiModel
import com.yugyd.quiz.gameui.game.model.RewardedAdStatus
import com.yugyd.quiz.ui.game.api.model.BaseQuestUiModel

internal interface GameView {

    data class State(
        val payload: GamePayload,
        val domainQuest: BaseQuestDomainModel? = null,
        val quest: BaseQuestUiModel? = null,
        val control: ControlUiModel = ControlUiModel(),
        val bannerState: BannerState? = null,
        val error: Throwable? = null,
        val isAdFeatureEnabled: Boolean = false,
        val isProFeatureEnabled: Boolean = false,
        val isWarning: Boolean = false,
        val isLoading: Boolean = false,
        val showErrorMessage: Boolean = false,
        val showRewardedAdNotLoadMessage: Boolean = false,
        val debugTrueAnswer: String? = null,
        val scrollToTopAnimation: Boolean = false,
        val showRewardedDialog: Boolean = false,
        val startErrorVibration: Boolean = false,
        val navigationState: NavigationState? = null,
        val manualAnswer: String = "",
        // Ad
        val rewardedAdStatus: RewardedAdStatus = RewardedAdStatus.NONE,
    ) {

        sealed interface BannerState {

            data class PromoBannerState(
                val proMessage: TextModel,
            ) : BannerState

            data class AdBannerState(
                val bannerAdUnitId: TextModel,
                val proMessage: TextModel,
            ) : BannerState
        }

        sealed interface NavigationState {
            object Back : NavigationState
            object NavigateToPro : NavigationState
            data class NavigateToProgressEnd(val payload: GameEndPayload) : NavigationState
            data class NavigateToGameEnd(val payload: GameEndPayload) : NavigationState
        }
    }

    sealed interface Action {

        // Ad Banner
        object OnProBannerClicked : Action

        object OnBannerAdLoaded : Action

        class OnBannerAdFailedToLoad(val adError: AdErrorDomainModel) : Action

        // Ad Reward
        object OnUserEarnedReward : Action

        object OnRewardAdLoad : Action

        class OnRewardAdFailedToLoad(val adError: AdErrorDomainModel? = null) : Action

        object OnRewardAdNotShowed : Action

        object OnRewardAdClosed : Action

        // Reward dialog
        object OnPositiveRewardDialogClicked : Action

        object OnNegativeRewardDialogClicked : Action

        object OnRewardDialogDismissed : Action

        // Screen
        object OnBackPressed : Action

        class OnAnswerClicked(val index: Int) : Action

        object OnSnackbarDismissed : Action

        object OnDebugAnswerToastDismissed : Action

        object OnScrollToTopAnimationEnded : Action

        object OnErrorVibrationEnded : Action

        object OnNavigationHandled : Action

        data class OnAnswerTextChanged(val userAnswer: String) : Action
    }
}
