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

package com.yugyd.quiz.ui.errors

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.yugyd.quiz.domain.api.payload.ErrorListPayload
import com.yugyd.quiz.navigation.IntListDecoder
import com.yugyd.quiz.navigation.hideBottomBarArgument

private const val ERROR_IDS_ARG = "errorQuestIds"
private const val ERROR_LIST_ROUTE = "error_list/"

internal class ErrorListArgs(
    val errorListPayload: ErrorListPayload
) {

    constructor(savedStateHandle: SavedStateHandle) : this(
        errorListPayload = ErrorListPayload(
            errorQuestIds = IntListDecoder.decode(
                value = checkNotNull(savedStateHandle[ERROR_IDS_ARG])
            ),
        )
    )
}

fun NavController.navigateToErrorList(payload: ErrorListPayload) {
    val idsArgument = IntListDecoder.encode(payload.errorQuestIds)
    navigate("$ERROR_LIST_ROUTE${idsArgument}")
}

fun NavGraphBuilder.errorListScreen(
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onNavigateToBrowser: (String) -> Unit,
) {
    composable(
        route = "$ERROR_LIST_ROUTE{$ERROR_IDS_ARG}",
        arguments = listOf(
            navArgument(ERROR_IDS_ARG) { type = NavType.StringType },
            hideBottomBarArgument,
        ),
    ) {
        ErrorListRoute(
            snackbarHostState = snackbarHostState,
            onBack = onBack,
            onNavigateToBrowser = onNavigateToBrowser,
        )
    }
}
