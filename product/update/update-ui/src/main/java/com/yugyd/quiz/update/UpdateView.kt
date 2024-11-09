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

package com.yugyd.quiz.update

internal interface UpdateView {

    data class State(
        val updateConfig: UpdateConfigUiModel = UpdateConfigUiModel(),
        val isLoading: Boolean = false,
        val navigationState: NavigationState? = null,
    ) {

        data class UpdateConfigUiModel(
            val buttonTitle: String = "",
            val message: String = "",
            val title: String = "",
        )

        sealed interface NavigationState {
            data class NavigateToGooglePlay(val storeLink: String? = null) : NavigationState
        }
    }

    sealed interface Action {
        data object OnUpdateClicked : Action
        data object OnNavigationHandled : Action
    }
}
