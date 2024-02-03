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

package com.yugyd.quiz.ui.end.progressend

import com.yugyd.quiz.domain.api.model.payload.GameEndPayload

internal interface ProgressEndView {

    data class State(
        val payload: GameEndPayload,
        val isRated: Boolean = false,
        val title: String = "",
        val message: String = "",
        val actionButtonTitle: String = "",
        val actionButtonType: ActionButtonType = ActionButtonType.RATE,
        val showTelegram: Boolean = false,
        val telegramLink: String = "",
        val navigationState: NavigationState? = null,
    ) {
        enum class ActionButtonType {
            RATE, TELEGRAM
        }

        sealed interface NavigationState {
            object Back : NavigationState
            object NavigateToGooglePlay : NavigationState
            data class NavigateToGameEnd(val payload: GameEndPayload) : NavigationState
        }
    }

    sealed interface Action {
        object OnRateClicked : Action
        object OnSkipClicked : Action
        object OnTelegramHandled : Action
        object OnNavigationHandled : Action
    }
}
