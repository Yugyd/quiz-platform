/*
 * Copyright 2024 Roman Likhachev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yugyd.quiz.ui.aisettings

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.yugyd.quiz.domain.api.payload.AiConnectionDetailsPayload

private const val AI_SETTINGS_ROUTE = "ai_settings"

fun NavController.navigateToAiSettings() {
    navigate(AI_SETTINGS_ROUTE)
}

fun NavGraphBuilder.aiSettingsScreen(
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onNavigateToAiConnectionDetails: (AiConnectionDetailsPayload) -> Unit,
    onNavigateToBrowser: (String) -> Unit,
) {
    composable(route = AI_SETTINGS_ROUTE) {
        AiSettingsRoute(
            snackbarHostState = snackbarHostState,
            onBack = onBack,
            onNavigateToAiConnectionDetails = onNavigateToAiConnectionDetails,
            onNavigateToBrowser = onNavigateToBrowser,
        )
    }
}
