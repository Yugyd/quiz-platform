/*
 * Copyright 2024 Roman Likhachev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yugyd.quiz.ui.aisettings

import com.yugyd.quiz.ai.connection.api.model.AiConnectionModel
import com.yugyd.quiz.ui.aisettings.model.AiConnectionUiModel

internal interface AiSettingsView {

    data class State(
        val domainItems: List<AiConnectionModel> = emptyList(),
        val isInfoDialogVisible: Boolean = false,
        val isTermOfCacheDialogVisible: Boolean = false,
        val isAiEnabled: Boolean = false,
        val items: List<AiConnectionUiModel> = emptyList(),
        val termOfCacheValue: Int = 0,
        val newTermOfCacheValue: Int? = null,
        val termOfCacheOptions: List<Int> = emptyList(),
        val isDirectConnectionEnabled: Boolean = false,
        val isAddConnectionVisible: Boolean = true,
        val aiPrivacyUrl: String? = null,
        val isWarning: Boolean = false,
        val isLoading: Boolean = false,
        val showErrorMessage: Boolean = false,
        val navigationState: NavigationState? = null,
    ) {

        sealed interface NavigationState {
            data object Back : NavigationState
            data class NavigateToAiConnectionDetails(val id: String?) : NavigationState
            data class NavigateToAiUsagePolicy(val aiPrivacyUrl: String) : NavigationState
        }
    }

    sealed interface Action {
        data object OnBackClicked : Action
        data object OnInfoClicked : Action
        data class OnAiEnabledChecked(val isAiEnabled: Boolean) : Action
        data class OnAiConnectionClicked(val item: AiConnectionUiModel) : Action
        data class OnEditAiConnectionClicked(val item: AiConnectionUiModel) : Action
        data object OnAddAiConnectionClicked : Action
        data object OnUpdateTermClicked : Action
        data class OnDirectAiConnectionChecked(val isDirectConnectionEnabled: Boolean) : Action
        data object OnSnackbarDismissed : Action
        data object OnInfoDialogDismissed : Action
        data object OnNavigationHandled : Action
        data class OnTermOfCacheOptionSelected(val option: String) : Action
        data object OnTermOfCacheSaveClicked : Action
        data object OnTermOfCacheDialogDismissed : Action
        data object OnAiUsagePolicyClicked : Action
    }
}
