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

package com.yugyd.quiz.ui.end.gameend

import androidx.compose.ui.graphics.Color
import com.yugyd.quiz.ad.api.AdErrorDomainModel
import com.yugyd.quiz.core.TextModel
import com.yugyd.quiz.domain.api.model.payload.GameEndPayload
import com.yugyd.quiz.domain.api.payload.ErrorListPayload

internal interface GameEndView {

    data class State(
        val payload: GameEndPayload,
        val themeTitle: String = "",
        val progress: Int = 0,
        val progressTitle: String = "",
        val progressColor: Color = Color.Unspecified,
        val isErrorVisible: Boolean = false,
        val isAdFeatureEnabled: Boolean = false,
        val isLoading: Boolean = false,
        val navigationState: NavigationState? = null,
        // Ad
        val interstitialAdState: InterstitialAdState? = null,
        val interstitialAdUnitId: TextModel? = null,
        val showInterstitialErrorMessage: Boolean = false,
    ) {

        enum class InterstitialAdState {
            LOAD_AD,
            IS_LOADED,
            NOT_LOADED,
            SHOW_AD,
        }

        sealed interface NavigationState {
            object Back : NavigationState
            data class NavigateToErrorList(val payload: ErrorListPayload) : NavigationState
        }
    }

    sealed interface Action {
        object OnBackClicked : Action
        object OnNewGameClicked : Action
        object OnShowErrorsClicked : Action
        object OnNavigationHandled : Action
        object OnSnackbarDismissed : Action

        // Ad
        class OnInterstitialAdFailedToLoad(val adError: AdErrorDomainModel? = null) : Action

        class OnInterstitialAdFailedToShow(val adError: AdErrorDomainModel? = null) : Action

        object OnInterstitialAdNotToLoad : Action

        object OnInterstitialAdClosed : Action

        object OnInterstitialAdLoaded : Action
    }
}