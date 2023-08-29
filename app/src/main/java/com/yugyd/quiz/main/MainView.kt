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

package com.yugyd.quiz.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.yugyd.quiz.domain.api.payload.OnboardingPayload
import com.yugyd.quiz.uikit.R
import com.yugyd.quiz.commonui.R as CommonUiR

interface MainView {

    data class State(
        val isCorrectFeatureEnabled: Boolean = false,
        val topDestinations: List<TopDestination> = TopDestination.values().asList(),
        val requestPushPermission: Boolean = false,
        val showOnboarding: Boolean = false,
        val onboardingPayload: OnboardingPayload? = null,
        val showTelegram: Boolean = false,
        val telegramLink: String = "",
        val navigationState: NavigationState? = null,
    ) {

        enum class TopDestination(
            @StringRes val titleResId: Int,
            @DrawableRes val iconResId: Int,
        ) {

            THEME(
                R.string.title_theme,
                CommonUiR.drawable.ic_menu_book,
            ),

            CORRECT(
                R.string.title_correct,
                CommonUiR.drawable.ic_check_circle,
            ),

            PROGRESS(
                R.string.title_progress,
                CommonUiR.drawable.ic_pie_chart,
            ),

            PROFILE(
                R.string.title_profile,
                R.drawable.ic_account_circle,
            ),
        }

        sealed interface NavigationState {
            object Back : NavigationState
            object NavigateToUpdate : NavigationState
        }
    }

    sealed interface Action {
        object OnOnboardingClicked : Action
        object OnBackPressed : Action
        data class ProcessPushData(val extras: Map<String, String>) : Action
        object RequestPushPermissionHandled : Action
        object OnboardingBottomSheetDismissed : Action
        object OnTelegramHandled : Action
        object OnNavigationHandled : Action
    }
}
