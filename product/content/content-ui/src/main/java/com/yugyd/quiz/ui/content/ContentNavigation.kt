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

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.yugyd.quiz.navigation.hideBottomBarArgument

internal const val IS_BACK_ENABLED_ARG = "isBackEnabled"
private const val ROUTE = "content"

internal class ContentArgs(
    val isBackEnabled: Boolean,
) {

    constructor(savedStateHandle: SavedStateHandle) : this(
        isBackEnabled = checkNotNull(savedStateHandle[IS_BACK_ENABLED_ARG])
    )
}

fun NavController.navigateToContent(isBackEnabled: Boolean) {
    val route = "$ROUTE/${isBackEnabled}"
    navigate(route)
}

fun NavGraphBuilder.contentScreen(
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onNavigateToBrowser: (String) -> Unit,
) {
    val route = "$ROUTE/{$IS_BACK_ENABLED_ARG}"
    composable(
        route = route,
        arguments = listOf(
            navArgument(IS_BACK_ENABLED_ARG) { type = NavType.BoolType },
            hideBottomBarArgument,
        ),
    ) {
        ContentRoute(
            snackbarHostState = snackbarHostState,
            onBack = onBack,
            onNavigateToBrowser = onNavigateToBrowser,
        )
    }
}
