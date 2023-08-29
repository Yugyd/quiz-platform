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

import com.google.android.gms.ads.LoadAdError
import com.yugyd.quiz.core.GlobalConfig
import com.yugyd.quiz.domain.api.model.game.QuestModel
import com.yugyd.quiz.domain.api.model.payload.GameEndPayload
import com.yugyd.quiz.domain.api.model.payload.GamePayload
import com.yugyd.quiz.gameui.game.model.ControlUiModel
import com.yugyd.quiz.gameui.game.model.QuestUiModel
import com.yugyd.quiz.gameui.game.model.RewardedAdStatus

interface GameView {

    data class State(
        val payload: GamePayload,
        val domainQuest: QuestModel = QuestModel(),
        val rewardedAdStatus: RewardedAdStatus = RewardedAdStatus.NONE,
        val quest: QuestUiModel = QuestUiModel(),
        val control: ControlUiModel = ControlUiModel(),
        val adBannerState: AdBannerState = AdBannerState.LOADING,
        val error: Throwable? = null,
        val isAdFeatureEnabled: Boolean = false,
        val isProFeatureEnabled: Boolean = false,
        val isWarning: Boolean = false,
        val isLoading: Boolean = false,
        val showErrorMessage: Boolean = false,
        val showRewardedAd: Boolean = false,
        val showRewardedAdNotLoadMessage: Boolean = false,
        val loadAd: Boolean = false,
        val showAdBannerAnimation: Boolean = false,
        val showDebugAnswerToast: Boolean = false,
        val debugTrueAnswer: String? = null,
        val scrollToTopAnimation: Boolean = false,
        val showRewardedDialog: Boolean = false,
        val startErrorVibration: Boolean = false,
        val isDebugMode: Boolean = GlobalConfig.DEBUG,
        val navigationState: NavigationState? = null,
    ) {

        enum class AdBannerState {
            AD, PROMO, LOADING
        }

        sealed interface NavigationState {
            object Back : NavigationState
            object NavigateToPro : NavigationState
            data class NavigateToProgressEnd(val payload: GameEndPayload) : NavigationState
            data class NavigateToGameEnd(val payload: GameEndPayload) : NavigationState
        }
    }

    sealed interface Action {
        object OnProBannerClicked : Action
        object OnBackPressed : Action
        class OnAnswerClicked(val index: Int) : Action
        object OnPositiveRewardDialogClicked : Action
        object OnNegativeRewardDialogClicked : Action
        object OnUserEarnedReward : Action
        object OnRewardAdLoad : Action
        class OnRewardAdFailedToLoad(val adError: LoadAdError? = null) : Action
        object OnRewardAdNotShowed : Action
        object OnRewardAdClosed : Action
        object OnBannerAdLoaded : Action
        class OnBannerAdFailedToLoad(val adError: LoadAdError) : Action
        object OnAutoTestClicked : Action
        object OnAutoTestLongClicked : Action
        object OnSnackbarDismissed : Action
        object OnAdBannerAnimationEnded : Action
        object OnDebugAnswerToastDismissed : Action
        object OnScrollToTopAnimationEnded : Action
        object OnRewardDialogDismissed : Action
        object OnErrorVibrationEnded : Action
        object OnNavigationHandled : Action
    }
}
