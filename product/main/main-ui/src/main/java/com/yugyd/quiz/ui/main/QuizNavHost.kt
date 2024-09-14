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

package com.yugyd.quiz.ui.main

import android.content.Intent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.yugyd.quiz.commonui.providers.LocalResIdProvider
import com.yugyd.quiz.correctui.correctScreen
import com.yugyd.quiz.gameui.game.gameScreen
import com.yugyd.quiz.gameui.game.navigateToGame
import com.yugyd.quiz.navigation.getTelegramIntent
import com.yugyd.quiz.progressui.progress.progressScreen
import com.yugyd.quiz.progressui.specificprogress.navigateToSpecificProgress
import com.yugyd.quiz.progressui.specificprogress.specificProgressScreen
import com.yugyd.quiz.proui.proonboarding.navigateToProOnboarding
import com.yugyd.quiz.proui.proonboarding.proOnboardingScreen
import com.yugyd.quiz.ui.content.contentScreen
import com.yugyd.quiz.ui.content.navigateToContent
import com.yugyd.quiz.ui.end.gameend.gameEndScreen
import com.yugyd.quiz.ui.end.gameend.navigateToGameEnd
import com.yugyd.quiz.ui.end.progressend.navigateToProgressEnd
import com.yugyd.quiz.ui.end.progressend.progressEndScreen
import com.yugyd.quiz.ui.errors.errorListScreen
import com.yugyd.quiz.ui.errors.navigateToErrorList
import com.yugyd.quiz.ui.profile.profileScreen
import com.yugyd.quiz.ui.section.navigateToSection
import com.yugyd.quiz.ui.section.sectionScreen
import com.yugyd.quiz.ui.tasks.navigateToTasks
import com.yugyd.quiz.ui.tasks.tasksScreen
import com.yugyd.quiz.ui.theme.THEME_ROUTE
import com.yugyd.quiz.ui.theme.themeScreen
import com.yugyd.quiz.ui.transition.navigateToTransition
import com.yugyd.quiz.ui.transition.transitionScreen
import com.yugyd.quiz.update.updateScreen

@Composable
internal fun QuizNavHost(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    navigateToExternalScreen: (Intent) -> Unit,
) {
    val context = LocalContext.current
    val resIdProvider = LocalResIdProvider.current

    NavHost(
        navController = navController,
        startDestination = THEME_ROUTE,
    ) {
        contentScreen(
            snackbarHostState = snackbarHostState,
            onBack = navController::popBackStack,
            onNavigateToBrowser = {
                navigateToExternalScreen(GlobalScreens.externalBrowser(it))
            }
        )

        updateScreen(
            navigateToGooglePlay = {
                navigateToExternalScreen(GlobalScreens.rate())
            },
        )

        sectionScreen(
            snackbarHostState = snackbarHostState,
            onNavigateToGame = navController::navigateToGame,
            onBack = navController::popBackStack,
        )

        themeScreen(
            snackbarHostState = snackbarHostState,
            onNavigateToGame = navController::navigateToGame,
            onNavigateToSection = navController::navigateToSection,
            onNavigateToTelegram = {
                navigateToExternalScreen(context.getTelegramIntent(it))
            },
        )

        progressScreen(
            snackbarHostState = snackbarHostState,
            onNavigateToSpecificProgress = navController::navigateToSpecificProgress,
        )

        specificProgressScreen(
            snackbarHostState = snackbarHostState,
            onBack = navController::popBackStack,
        )

        transitionScreen(
            onBack = navController::popBackStack,
        )

        profileScreen(
            onNavigateToTelegram = {
                navigateToExternalScreen(context.getTelegramIntent(it))
            },
            onNavigateToTransition = navController::navigateToTransition,
            onNavigateToExternalReportError = {
                navigateToExternalScreen(GlobalScreens.externalReportError(context))
            },
            onNavigateToGooglePlay = {
                navigateToExternalScreen(GlobalScreens.rate())
            },
            onNavigateToOtherApps = {
                navigateToExternalScreen(GlobalScreens.otherApps())
            },
            onNavigateToPrivacyPolicy = {
                val privacyPolicy = context.getString(resIdProvider.appPrivacyPolicyLink())
                navigateToExternalScreen(GlobalScreens.privacyPolicy(privacyPolicy))
            },
            onNavigateToProOnboarding = navController::navigateToProOnboarding,
            onNavigateToShare = {
                navigateToExternalScreen(GlobalScreens.share())
            },
            onNavigateToContents = {
                navController.navigateToContent(isBackEnabled = true)
            },
            onNavigateToExternalPlatformRate = {
                navigateToExternalScreen(GlobalScreens.platformGitHubProject(context))
            },
            onNavigateToExternalPlatformReportError = {
                navigateToExternalScreen(GlobalScreens.platformGitHubIssues(context))
            },
            onNavigateToTasks = {
                navController.navigateToTasks()
            }
        )

        proOnboardingScreen(
            onNavigateToPro = {
                navigateToExternalScreen(GlobalScreens.pro())
            },
            onBack = navController::popBackStack,
        )

        errorListScreen(
            snackbarHostState = snackbarHostState,
            onBack = navController::popBackStack,
            onNavigateToBrowser = {
                navigateToExternalScreen(GlobalScreens.externalBrowser(it))
            }
        )

        gameScreen(
            snackbarHostState = snackbarHostState,
            onNavigateToGameEnd = navController::navigateToGameEnd,
            onNavigateToProOnboarding = navController::navigateToProOnboarding,
            onNavigateToProgressEnd = navController::navigateToProgressEnd,
            onBack = navController::popBackStack,
        )

        gameEndScreen(
            snackbarHostState = snackbarHostState,
            onNavigateToErrors = navController::navigateToErrorList,
            onBack = navController::popBackStack,
        )

        progressEndScreen(
            onNavigateToGameEnd = navController::navigateToGameEnd,
            onNavigateToRate = {
                navigateToExternalScreen(GlobalScreens.rate())
            },
            onBack = navController::popBackStack,
            onNavigateToTelegram = {
                navigateToExternalScreen(context.getTelegramIntent(it))
            },
        )

        correctScreen(
            snackbarHostState = snackbarHostState,
            onNavigateToGame = navController::navigateToGame,
        )

        tasksScreen(
            snackbarHostState = snackbarHostState,
            onBack = navController::popBackStack,
            onNavigateToBrowser = {
                navigateToExternalScreen(GlobalScreens.externalBrowser(it))
            },
            onNavigateToGame = navController::navigateToGame,
        )
    }
}
