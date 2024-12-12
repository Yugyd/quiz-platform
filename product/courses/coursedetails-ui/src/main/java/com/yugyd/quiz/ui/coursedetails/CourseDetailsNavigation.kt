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

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.yugyd.quiz.domain.api.payload.AiTasksPayload
import com.yugyd.quiz.domain.api.payload.CourseDetailsPayload
import com.yugyd.quiz.navigation.hideBottomBarArgument

private const val COURSE_DETAILS_ID_ARG = "courseId"
private const val COURSE_DETAILS_TITLE_ARG = "courseTitle"
private const val COURSE_DETAILS_ROUTE = "course_details/"

internal class CourseDetailsArgs(
    val courseDetailsPayload: CourseDetailsPayload,
) {

    constructor(savedStateHandle: SavedStateHandle) : this(
        courseDetailsPayload = CourseDetailsPayload(
            courseId = checkNotNull(savedStateHandle[COURSE_DETAILS_ID_ARG]),
            courseTitle = checkNotNull(savedStateHandle[COURSE_DETAILS_TITLE_ARG]),
        ),
    )
}

fun NavController.navigateToCourseDetails(
    payload: CourseDetailsPayload,
) {
    navigate("$COURSE_DETAILS_ROUTE${payload.courseId}&${payload.courseTitle}")
}

fun NavGraphBuilder.courseDetailsScreen(
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onNavigateToAiTasks: (AiTasksPayload) -> Unit,
    onNavigateToExternalReportError: () -> Unit,
) {
    val route =
        "$COURSE_DETAILS_ROUTE{$COURSE_DETAILS_ID_ARG}&{$COURSE_DETAILS_TITLE_ARG}"
    composable(
        route = route,
        arguments = listOf(
            navArgument(COURSE_DETAILS_ID_ARG) { type = NavType.IntType },
            navArgument(COURSE_DETAILS_TITLE_ARG) { type = NavType.StringType },
            hideBottomBarArgument,
        )
    ) {
        CourseDetailsRoute(
            snackbarHostState = snackbarHostState,
            onBack = onBack,
            onNavigateToAiTasks = onNavigateToAiTasks,
            onNavigateToExternalReportError = onNavigateToExternalReportError,
        )
    }
}
