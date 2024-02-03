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

package com.yugyd.quiz.progressui.progress

import com.yugyd.quiz.domain.api.payload.SpecificProgressPayload
import com.yugyd.quiz.progressui.model.ItemProgressUiModel
import com.yugyd.quiz.progressui.model.ProgressUiModel

internal interface ProgressView {

    data class State(
        val models: List<Any> = emptyList(),
        val items: List<ProgressUiModel> = emptyList(),
        val isWarning: Boolean = false,
        val isLoading: Boolean = false,
        val showErrorMessage: Boolean = false,
        val navigationState: NavigationState? = null,
    ) {

        sealed interface NavigationState {
            data class NavigateToSpecificProgress(
                val payload: SpecificProgressPayload
            ) : NavigationState
        }
    }

    sealed interface Action {
        class OnProgressClicked(val item: ItemProgressUiModel) : Action
        object OnSnackbarDismissed : Action
        object OnNavigationHandled : Action
    }
}

