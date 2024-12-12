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

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.yugyd.quiz.domain.api.payload.CourseDetailsPayload
import com.yugyd.quiz.domain.api.payload.SubCoursePayload

private const val COURSE_ID_ARG = "courseId"
private const val COURSE_TITLE_ARG = "courseTitle"
private const val COURSE_HIDE_CONTINUE_BANNER_ARG = "courseHideContinueBanner"
private const val COURSE_LIST_ROUTE = "sub_course_list/"

internal class SubCourseArgs(
    val subCoursePayload: SubCoursePayload,
) {

    constructor(savedStateHandle: SavedStateHandle) : this(
        subCoursePayload = SubCoursePayload(
            courseId = checkNotNull(savedStateHandle[COURSE_ID_ARG]),
            courseTitle = checkNotNull(savedStateHandle[COURSE_TITLE_ARG]),
            isHideContinueBanner = checkNotNull(savedStateHandle[COURSE_HIDE_CONTINUE_BANNER_ARG]),
        ),
    )
}


fun NavController.navigateToSubCourseList(
    payload: SubCoursePayload,
    navOptions: NavOptions? = null,
) {
    navigate(
        route = "$COURSE_LIST_ROUTE${payload.courseId}&${payload.courseTitle}&${payload.isHideContinueBanner}",
        navOptions = navOptions,
    )
}

fun NavGraphBuilder.subCourseListScreen(
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToSubCourse: (SubCoursePayload) -> Unit,
    onNavigateToCourseDetails: (CourseDetailsPayload) -> Unit,
) {
    val route =
        "$COURSE_LIST_ROUTE{$COURSE_ID_ARG}&{$COURSE_TITLE_ARG}&{$COURSE_HIDE_CONTINUE_BANNER_ARG}"
    composable(
        route = route,
        arguments = listOf(
            navArgument(COURSE_ID_ARG) { type = NavType.IntType },
            navArgument(COURSE_TITLE_ARG) { type = NavType.StringType },
            navArgument(COURSE_HIDE_CONTINUE_BANNER_ARG) { type = NavType.BoolType },
        )
    ) {
        SubCourseListRoute(
            snackbarHostState = snackbarHostState,
            onBack = onBack,
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToSubCourse = onNavigateToSubCourse,
            onNavigateToCourseDetails = onNavigateToCourseDetails,
        )
    }
}
