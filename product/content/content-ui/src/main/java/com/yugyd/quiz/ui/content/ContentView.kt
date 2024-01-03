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

package com.yugyd.quiz.ui.content

import com.yugyd.quiz.domain.content.api.ContentModel
import com.yugyd.quiz.domain.content.exceptions.ContentVerificationException

interface ContentView {

    data class State(
        val isBackEnabled: Boolean = true,
        val items: List<ContentModel>? = null,
        val isWarning: Boolean = false,
        val isLoading: Boolean = false,
        val snackbarState: SnackbarState? = null,
        val startFileProvider: Boolean = false,
        val navigationState: NavigationState? = null,
    ) {

        sealed interface SnackbarState {
            object NotAddedContentIsExists : SnackbarState

            object UriIsNull : SnackbarState

            object AddIsFailed : SnackbarState

            // TODO Add the feature to ask for removal from the user
            object NotSelectAndDelete : SnackbarState

            object SelectIsFailed : SnackbarState

            object DeleteIsFailed : SnackbarState
            object OneItemNotDelete : SnackbarState
            object SelectedItemNotDelete : SnackbarState

            data class VerifyError(val error: ContentVerificationException) : SnackbarState

            object ContentFormatUrlNotLoaded : SnackbarState
        }

        sealed interface NavigationState {
            data class NavigateToContentFormat(val url: String) : NavigationState
            object Back : NavigationState
        }
    }

    sealed interface Action {
        object OnBackClicked : Action
        class OnItemClicked(val item: ContentModel) : Action
        class OnDeleteClicked(val item: ContentModel) : Action
        object OnOpenFileClicked : Action
        object OnContentFormatClicked : Action
        object OnSnackbarDismissed : Action
        object OnStartFileProviderHandled : Action
        class OnDocumentResult(val uri: String?) : Action
        object OnNavigationHandled : Action
    }
}