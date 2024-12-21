/*
 *    Copyright 2024 Roman Likhachev
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

package com.yugyd.quiz.ui.aiconnectiondetails

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.yugyd.quiz.domain.api.payload.AiConnectionDetailsPayload
import com.yugyd.quiz.navigation.hideBottomBarArgument

private const val AI_CONNECTION_DETAILS_ID_ARG = "aiConnectionId"
private const val AI_CONNECTION_DETAILS_ROUTE = "ai_connection_details/"

internal class AiConnectionDetailsArgs(
    val aiConnectionDetailsPayload: AiConnectionDetailsPayload,
) {

    constructor(savedStateHandle: SavedStateHandle) : this(
        aiConnectionDetailsPayload = AiConnectionDetailsPayload(
            aiConnectionId = savedStateHandle[AI_CONNECTION_DETAILS_ID_ARG],
        ),
    )
}

fun NavController.navigateToAiConnectionDetails(
    payload: AiConnectionDetailsPayload,
) {
    navigate("$AI_CONNECTION_DETAILS_ROUTE${payload.aiConnectionId}")
}

fun NavGraphBuilder.aiConnectionDetailsScreen(
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onNavigateToBrowser: (String) -> Unit,
) {
    val route = "$AI_CONNECTION_DETAILS_ROUTE{$AI_CONNECTION_DETAILS_ID_ARG}"
    composable(
        route = route,
        arguments = listOf(
            navArgument(AI_CONNECTION_DETAILS_ID_ARG) {
                type = NavType.StringType
                nullable = true
            },
            hideBottomBarArgument,
        )
    ) {
        AiConnectionDetailsRoute(
            snackbarHostState = snackbarHostState,
            onBack = onBack,
            onNavigateToBrowser = onNavigateToBrowser,
        )
    }
}
