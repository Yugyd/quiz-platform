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

package com.yugyd.quiz.ui.courses

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.yugyd.quiz.domain.api.payload.CourseDetailsPayload
import com.yugyd.quiz.domain.api.payload.SubCoursePayload
import com.yugyd.quiz.navigation.topLevelNavOptions

private const val COURSE_LIST_ROUTE = "course_list"

fun NavController.navigateToCourseList() {
    navigate(COURSE_LIST_ROUTE, topLevelNavOptions())
}

fun NavGraphBuilder.courseListScreen(
    snackbarHostState: SnackbarHostState,
    onNavigateToSearch: () -> Unit,
    onNavigateToSubCourse: (SubCoursePayload) -> Unit,
    onNavigateToCourseDetails: (CourseDetailsPayload) -> Unit,
) {
    composable(route = COURSE_LIST_ROUTE) {
        CourseListRoute(
            snackbarHostState = snackbarHostState,
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToSubCourse = onNavigateToSubCourse,
            onNavigateToCourseDetails = onNavigateToCourseDetails,
        )
    }
}
