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

package com.yugyd.quiz.ui.end.progressend

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.yugyd.quiz.domain.api.model.payload.GameEndPayload
import com.yugyd.quiz.navigation.replaceCurrentScreenNavOptions
import com.yugyd.quiz.ui.end.endScreen
import com.yugyd.quiz.ui.end.getEndRoute
import com.yugyd.quiz.ui.end.getEndRouteModel

private const val PROGRESS_END_ROUTE = "progress_end"

private val routeModel = getEndRouteModel(PROGRESS_END_ROUTE)

fun NavController.navigateToProgressEnd(payload: GameEndPayload) {
    val route = getEndRoute(routeModel, payload)
    navigate(route, replaceCurrentScreenNavOptions)
}

fun NavGraphBuilder.progressEndScreen(
    onNavigateToGameEnd: (GameEndPayload) -> Unit,
    onNavigateToRate: () -> Unit,
    onBack: () -> Unit,
    onNavigateToTelegram: (String) -> Unit,
) {
    endScreen(routeModel) {
        ProgressEndRoute(
            onNavigateToGameEnd = onNavigateToGameEnd,
            onNavigateToRate = onNavigateToRate,
            onBack = onBack,
            onNavigateToTelegram = onNavigateToTelegram,
        )
    }
}
