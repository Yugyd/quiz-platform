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

package com.yugyd.quiz.ui.section

import com.yugyd.quiz.domain.api.model.payload.GamePayload
import com.yugyd.quiz.domain.api.payload.SectionPayload
import com.yugyd.quiz.domain.section.model.Section

interface SectionView {

    data class State(
        val payload: SectionPayload = SectionPayload(),
        var models: List<Section> = emptyList(),
        val themeTitle: String = "",
        val error: Throwable? = null,
        val isWarning: Boolean = false,
        val isLoading: Boolean = false,
        val showErrorMessage: Boolean = false,
        val navigationState: NavigationState? = null,
    ) {

        sealed interface NavigationState {
            object Back : NavigationState
            data class NavigateToGame(val payload: GamePayload) : NavigationState
        }
    }

    sealed interface Action {
        object OnBackPressed : Action
        class OnSectionClicked(val item: Section) : Action
        object OnSnackbarDismissed : Action
        object OnNavigationHandled : Action
    }
}
