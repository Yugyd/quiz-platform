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

package com.yugyd.quiz.progressui.specificprogress

import com.yugyd.quiz.domain.model.payload.SpecificProgressPayload
import com.yugyd.quiz.progressui.model.ProgressUiModel

interface SpecificProgressView {

    data class State(
        val payload: SpecificProgressPayload,
        val models: List<Any> = emptyList(),
        val themeTitle: String = "",
        val items: List<ProgressUiModel> = emptyList(),
        val resetState: ResetState = ResetState.HIDE,
        val isWarning: Boolean = false,
        val isLoading: Boolean = false,
        val showErrorMessage: Boolean = false,
        val showResetDialog: Boolean = false,
        val navigationState: NavigationState? = null,
    ) {

        enum class ResetState {
            VISIBLE, HIDE
        }

        sealed interface NavigationState {
            object Back : NavigationState
        }
    }

    sealed interface Action {
        object OnBackPressed : Action
        object OnResetProgress : Action
        object OnResetProgressAccepted : Action
        object OnSnackbarDismissed : Action
        object OnResetDialogDismissed : Action
        object OnNavigationHandled : Action
    }
}
