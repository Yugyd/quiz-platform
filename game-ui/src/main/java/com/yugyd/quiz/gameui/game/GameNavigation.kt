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

package com.yugyd.quiz.gameui.game

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.yugyd.quiz.core.AdIdProvider
import com.yugyd.quiz.core.ResIdProvider
import com.yugyd.quiz.domain.model.payload.GameEndPayload
import com.yugyd.quiz.domain.model.payload.GamePayload
import com.yugyd.quiz.domain.model.share.Mode
import com.yugyd.quiz.navigation.hideBottomBarArgument

private const val MODE_ARG = "mode"
private const val THEME_ID_ARG = "themeId"
private const val SECTION_ID_ARG = "sectionId"
private const val RECORD_ARG = "record"
private const val GAME_ROUTE = "game/"

internal class GamePayloadArgs(val payload: GamePayload) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        payload = GamePayload(
            mode = Mode.fromId(
                id = checkNotNull(savedStateHandle[MODE_ARG])
            ),
            themeId = checkNotNull(savedStateHandle[THEME_ID_ARG]),
            sectionId = checkNotNull(savedStateHandle[SECTION_ID_ARG]),
            record = checkNotNull(savedStateHandle[RECORD_ARG])
        )
    )
}

fun NavController.navigateToGame(payload: GamePayload) {
    val route = buildString {
        append(GAME_ROUTE)
        append("${payload.mode.id}&")
        append("${payload.themeId}&")
        append("${payload.sectionId}&")
        append("${payload.record}")
    }
    navigate(route)
}

/**
 * @param onNavigateToProgressEnd должен быть replace
 * @param onNavigateToGameEnd должен быть replace
 */
fun NavGraphBuilder.gameScreen(
    snackbarHostState: SnackbarHostState,
    adIdProvider: AdIdProvider,
    resIdProvider: ResIdProvider,
    onNavigateToProOnboarding: () -> Unit,
    onNavigateToProgressEnd: (GameEndPayload) -> Unit,
    onNavigateToGameEnd: (GameEndPayload) -> Unit,
    onBack: () -> Unit,
) {
    val route = buildString {
        append(GAME_ROUTE)
        append("{$MODE_ARG}&")
        append("{$THEME_ID_ARG}&")
        append("{$SECTION_ID_ARG}&")
        append("{$RECORD_ARG}")
    }
    composable(
        route = route,
        arguments = listOf(
            navArgument(MODE_ARG) { type = NavType.IntType },
            navArgument(THEME_ID_ARG) { type = NavType.IntType },
            navArgument(SECTION_ID_ARG) { type = NavType.IntType },
            navArgument(RECORD_ARG) { type = NavType.IntType },
            hideBottomBarArgument,
        ),
    ) {
        GameRoute(
            snackbarHostState = snackbarHostState,
            adIdProvider = adIdProvider,
            resIdProvider = resIdProvider,
            onNavigateToProOnboarding = onNavigateToProOnboarding,
            onNavigateToProgressEnd = onNavigateToProgressEnd,
            onNavigateToGameEnd = onNavigateToGameEnd,
            onBack = onBack,
        )
    }
}
