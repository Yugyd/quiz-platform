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

package com.yugyd.quiz.ui.searchcourses

import com.yugyd.quiz.domain.courses.model.CourseModel

internal interface SearchCourseView {

    data class State(
        val searchState: SearchBarState = SearchBarState(),
        val parentCourseTitle: String = "",
        val items: List<CourseModel> = emptyList(),
        val isWarning: Boolean = false,
        val isLoading: Boolean = false,
        val showErrorMessage: Boolean = false,
        val navigationState: NavigationState? = null,
    ) {

        data class SearchBarState(
            val query: String = "",
            val currentRequestQuery: String = "",
            val suggestions: List<String> = emptyList(),
            val isEnabled: Boolean = true,
        )

        sealed interface NavigationState {
            data object Back : NavigationState

            data class NavigateToSubCourse(
                val id: Int,
                val title: String,
                val isHideContinueCourseBanner: Boolean,
            ) : NavigationState

            data class NavigateToCourseDetail(val id: Int, val title: String) : NavigationState
        }
    }

    sealed interface Action {
        data class OnCourseClicked(val item: CourseModel) : Action
        data class OnSearchQueryChanged(val query: String) : Action
        data class OnSearchClicked(val query: String?) : Action
        data object OnSnackbarDismissed : Action
        data object OnNavigationHandled : Action
        data object OnBackPressed : Action
    }
}
