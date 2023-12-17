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

package com.yugyd.quiz.ui.end

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.yugyd.quiz.domain.api.model.Mode
import com.yugyd.quiz.domain.api.model.payload.GameEndPayload
import com.yugyd.quiz.navigation.IntListDecoder
import com.yugyd.quiz.navigation.RouteModel
import com.yugyd.quiz.navigation.SHOW_BOTTOM_BAR_ARG
import com.yugyd.quiz.navigation.calculateRouteModel
import com.yugyd.quiz.navigation.getRouteWithArguments
import com.yugyd.quiz.navigation.hideBottomBarArgument

private const val MODE_ARG = "mode"
private const val THEME_ID_ARG = "themeId"
private const val OLD_RECORD_ARG = "oldRecord"
private const val POINT_ARG = "point"
private const val COUNT_ARG = "count"
private const val ERROR_IDS_ARG = "errorQuestIds"
private const val IS_REWARDED_SUCCESS_ARG = "isRewardedSuccess"
private const val IS_BLOCKED_INTERSTITIAL_ARG = "isBlockedInterstitial"

internal fun getEndRouteModel(route: String) = calculateRouteModel(
    route = route,
    arguments = listOf(
        navArgument(MODE_ARG) { type = NavType.IntType },
        navArgument(THEME_ID_ARG) { type = NavType.IntType },
        navArgument(OLD_RECORD_ARG) { type = NavType.IntType },
        navArgument(POINT_ARG) { type = NavType.IntType },
        navArgument(COUNT_ARG) { type = NavType.IntType },
        navArgument(ERROR_IDS_ARG) { type = NavType.StringType },
        navArgument(IS_REWARDED_SUCCESS_ARG) { type = NavType.BoolType },
        navArgument(IS_BLOCKED_INTERSTITIAL_ARG) { type = NavType.BoolType },
        hideBottomBarArgument,
    ),
)

internal class EndArgs(
    val payload: GameEndPayload,
) {

    constructor(savedStateHandle: SavedStateHandle) : this(
        payload = GameEndPayload(
            mode = Mode.fromId(
                id = checkNotNull(savedStateHandle[MODE_ARG]),
            ),
            themeId = checkNotNull(savedStateHandle[THEME_ID_ARG]),
            oldRecord = checkNotNull(savedStateHandle[OLD_RECORD_ARG]),
            point = checkNotNull(savedStateHandle[POINT_ARG]),
            count = checkNotNull(savedStateHandle[COUNT_ARG]),
            errorQuestIds = IntListDecoder.decode(
                value = checkNotNull(savedStateHandle[ERROR_IDS_ARG])
            ),
            isRewardedSuccess = checkNotNull(savedStateHandle[IS_REWARDED_SUCCESS_ARG]),
            isBlockedInterstitial = checkNotNull(savedStateHandle[IS_BLOCKED_INTERSTITIAL_ARG]),
        )
    )
}

fun getEndRoute(
    routeModel: RouteModel,
    payload: GameEndPayload
): String {
    return routeModel.getRouteWithArguments {
        when (it) {
            MODE_ARG -> payload.mode.id.toString()
            THEME_ID_ARG -> payload.themeId.toString()
            OLD_RECORD_ARG -> payload.oldRecord.toString()
            POINT_ARG -> payload.point.toString()
            COUNT_ARG -> payload.count.toString()
            ERROR_IDS_ARG -> IntListDecoder.encode(payload.errorQuestIds)
            IS_REWARDED_SUCCESS_ARG -> payload.isRewardedSuccess.toString()
            IS_BLOCKED_INTERSTITIAL_ARG -> payload.isBlockedInterstitial.toString()
            SHOW_BOTTOM_BAR_ARG -> false.toString()
            else -> throw IllegalArgumentException("Argument not founded")
        }
    }
}

fun NavGraphBuilder.endScreen(
    routeModel: RouteModel,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = routeModel.route,
        arguments = routeModel.arguments,
        content = content,
    )
}
