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

package com.yugyd.quiz.progressui.specificprogress

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.yugyd.quiz.domain.model.payload.SpecificProgressPayload
import com.yugyd.quiz.navigation.calculateRouteModel
import com.yugyd.quiz.navigation.getRouteWithArguments

private const val THEME_ID_ARG = "themeId"
private const val THEME_TITLE_ARG = "themeTitle"
private const val SPECIFIC_PROGRESS_ROUTE = "specific_progress"

private val specificProgressRouteModel = calculateRouteModel(
    route = SPECIFIC_PROGRESS_ROUTE,
    arguments = listOf(
        navArgument(THEME_ID_ARG) { type = NavType.IntType },
        navArgument(THEME_TITLE_ARG) { type = NavType.StringType }
    ),
)

internal class SpecificProgressArgs(
    val payload: SpecificProgressPayload,
) {

    constructor(savedStateHandle: SavedStateHandle) : this(
        payload = SpecificProgressPayload(
            themeId = checkNotNull(savedStateHandle[THEME_ID_ARG]),
            themeTitle = checkNotNull(savedStateHandle[THEME_TITLE_ARG]),
        )
    )
}

fun NavController.navigateToSpecificProgress(payload: SpecificProgressPayload) {
    val route = specificProgressRouteModel.getRouteWithArguments {
        when (it) {
            THEME_ID_ARG -> payload.themeId.toString()
            THEME_TITLE_ARG -> payload.themeTitle
            else -> error("Argument not founded")
        }
    }
    navigate(route)
}

fun NavGraphBuilder.specificProgressScreen(
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
) {
    composable(
        route = specificProgressRouteModel.route,
        arguments = specificProgressRouteModel.arguments,
    ) {
        SpecificProgressRoute(
            snackbarHostState = snackbarHostState,
            onBack = onBack,
        )
    }
}
