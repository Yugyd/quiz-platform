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

package com.yugyd.quiz.ui.coursedetails

import androidx.compose.runtime.Immutable
import com.yugyd.quiz.domain.api.payload.CourseDetailsPayload
import com.yugyd.quiz.domain.courses.model.CourseDetailModel

internal interface CourseDetailsView {

    data class State(
        val courseDetailsDomainState: CourseDetailsDomainState = CourseDetailsDomainState(),
        val courseTitle: String = "",
        val isActionsVisible: Boolean = false,
        val isWarning: Boolean = false,
        val isLoading: Boolean = false,
        val showErrorMessage: SnackbarMessage? = null,
        val navigationState: NavigationState? = null,
    ) {

        enum class SnackbarMessage {
            ERROR, AI_TASKS_EMPTY, AI_TASKS_ERROR, AI_UNAUTHORIZED,
        }

        @Immutable
        data class CourseDetailsDomainState(
            val payload: CourseDetailsPayload = CourseDetailsPayload(),
            val courseDetailModel: CourseDetailModel? = null,
            val isAiTasksEnabled: Boolean = false,
        )

        sealed interface NavigationState {
            data object Back : NavigationState
            data object NavigateToExternalPlatformReportError : NavigationState
            data class NavigateToTasks(val id: Int, val title: String) : NavigationState
        }
    }

    sealed interface Action {
        data object OnTasksClicked : Action
        data object OnReportClicked : Action
        data object OnSnackbarDismissed : Action
        data object OnNavigationHandled : Action
        data object OnBackPressed : Action
    }
}
