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

package com.yugyd.quiz.ui.subcourses

import com.yugyd.quiz.domain.api.payload.SubCoursePayload
import com.yugyd.quiz.domain.courses.model.CourseModel
import com.yugyd.quiz.ui.commoncourses.models.ContinueCourseBannerUiModel

internal interface SubCourseListView {

    data class State(
        val payload: SubCoursePayload = SubCoursePayload(),
        val parentCourseTitle: String = "",
        val items: List<CourseModel> = emptyList(),
        val isWarning: Boolean = false,
        val isLoading: Boolean = false,
        val continueCourseBanner: ContinueCourseBannerUiModel? = null,
        val showErrorMessage: Boolean = false,
        val navigationState: NavigationState? = null,
    ) {

        sealed interface NavigationState {
            data object Back : NavigationState

            data object NavigateToSearch : NavigationState

            data class NavigateToSubCourse(
                val id: Int,
                val title: String,
                val isHideContinueBanner: Boolean
            ) : NavigationState

            data class NavigateToCourseDetail(val id: Int, val title: String) : NavigationState
        }
    }

    sealed interface Action {
        data object OnSearchClicked : Action
        data object OnContinueThemeBannerClicked : Action
        data object OnHideThemeBannerClicked : Action
        data class OnCourseClicked(val item: CourseModel) : Action
        data object OnSnackbarDismissed : Action
        data object OnNavigationHandled : Action
        data object OnBackPressed : Action
    }
}
