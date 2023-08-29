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

package com.yugyd.quiz.themeui.section

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.yugyd.quiz.domain.model.payload.GamePayload
import com.yugyd.quiz.domain.model.payload.SectionPayload

private const val THEME_ID_ARG = "themeId"
private const val THEME_TITLE_ARG = "themeTitle"
private const val SECTION_ROUTE = "section"

internal class SectionsArgs(
    val payload: SectionPayload
) {

    constructor(savedStateHandle: SavedStateHandle) : this(
        payload = SectionPayload(
            themeId = checkNotNull(savedStateHandle[THEME_ID_ARG]),
            themeTitle = checkNotNull(savedStateHandle[THEME_TITLE_ARG]),
        )
    )
}

fun NavController.navigateToSection(payload: SectionPayload) {
    val route = "$SECTION_ROUTE/${payload.themeId}&${payload.themeTitle}"
    navigate(route)
}

fun NavGraphBuilder.sectionScreen(
    snackbarHostState: SnackbarHostState,
    onNavigateToGame: (GamePayload) -> Unit,
    onBack: () -> Unit,
) {
    val route = "$SECTION_ROUTE/{$THEME_ID_ARG}&{$THEME_TITLE_ARG}"
    composable(
        route = route,
        arguments = listOf(
            navArgument(THEME_ID_ARG) { type = NavType.IntType },
            navArgument(THEME_TITLE_ARG) { type = NavType.StringType }
        ),
    ) {
        SectionRoute(
            snackbarHostState = snackbarHostState,
            onNavigateToGame = onNavigateToGame,
            onBack = onBack,
        )
    }
}
