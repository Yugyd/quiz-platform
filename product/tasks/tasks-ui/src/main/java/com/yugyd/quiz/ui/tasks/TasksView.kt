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

package com.yugyd.quiz.ui.tasks

import androidx.annotation.StringRes
import com.yugyd.quiz.domain.tasks.model.FilterModel
import com.yugyd.quiz.domain.tasks.model.TaskModel
import com.yugyd.quiz.ui.tasks.TasksView.State.FilterUiModel

internal interface TasksView {

    data class State(
        val allItems: List<TaskModel> = emptyList(),
        val isWarning: Boolean = false,
        val isLoading: Boolean = false,
        val allFilters: List<FilterUiModel> = emptyList(),
        val showFilters: Boolean = false,
        val showErrorMessage: Boolean = false,
        val navigationState: NavigationState? = null,
    ) {

        data class FilterUiModel(
            val filterModel: FilterModel,
            @StringRes val titleRes: Int,
        )

        sealed interface NavigationState {
            object Back : NavigationState
            data class NavigateToExternalBrowser(val url: String) : NavigationState
        }
    }

    sealed interface Action {
        object OnBackClicked : Action
        object OnShowFilterClicked : Action
        class OnTaskClicked(val item: TaskModel) : Action
        class OnFavoriteClicked(val item: TaskModel) : Action
        class OnFilterClicked(val item: FilterUiModel) : Action
        object OnSnackbarDismissed : Action
        object OnFiltersDismissed : Action
        object OnNavigationHandled : Action
    }
}
