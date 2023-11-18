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

package ${packageName}

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ${generalPackage}.domain.api.payload.${payloadName}
import ${generalPackage}.navigation.hideBottomBarArgument

private const val ID_ARG = "id"
private const val ROUTE = "${constantRouteName}/"

internal class ${argsName}Args(
    val payload: ${payloadName},
) {

    constructor(savedStateHandle: SavedStateHandle) : this(
        payload = ${payloadName}(
            id = checkNotNull(savedStateHandle[ID_ARG]),
        )
    )
}

fun NavController.navigateTo${featurePrefix}(payload: ${payloadName}) {
    navigate("$ROUTE$\{payload.id\}")
}

fun NavGraphBuilder.${navigationScreenName}(
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onNavigateToBrowser: (String) -> Unit,
) {
    composable(
        route = "\$ROUTE{\$ID_ARG}",
        arguments = listOf(
            navArgument(ID_ARG) { type = NavType.StringType },
            hideBottomBarArgument,
        ),
    ) {
        ${routeName}(
            snackbarHostState = snackbarHostState,
            onBack = onBack,
            onNavigateToBrowser = onNavigateToBrowser,
        )
    }
}
