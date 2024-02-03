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

package com.yugyd.quiz.proui.proonboarding

import com.yugyd.quiz.commonui.base.BaseViewModel
import com.yugyd.quiz.core.Logger
import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import com.yugyd.quiz.proui.proonboarding.ProOnboardingView.Action
import com.yugyd.quiz.proui.proonboarding.ProOnboardingView.State
import com.yugyd.quiz.proui.proonboarding.ProOnboardingView.State.NavigationState
import com.yugyd.quiz.proui.proonboarding.model.ProOnboadringUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class ProOnboardingViewModel @Inject constructor(
    private val proOnboadringUiMapper: ProOnboadringUiMapper,
    logger: Logger,
    dispatchersProvider: DispatchersProvider,
) : BaseViewModel<State, Action>(
    logger = logger,
    dispatchersProvider = dispatchersProvider,
    State(),
) {

    init {
        loadData()
    }

    override fun handleAction(action: Action) {
        when (action) {
            Action.OnBackClicked -> onBackClicked()
            Action.OnContinueClicked -> onContinueClicked()
            Action.OnNavigationHandled -> {
                screenState = screenState.copy(navigationState = null)
            }
        }
    }

    private fun onBackClicked() {
        screenState = screenState.copy(navigationState = NavigationState.Back)
    }

    private fun onContinueClicked() {
        screenState = screenState.copy(navigationState = NavigationState.NavigateToPro)
    }

    private fun loadData() {
        screenState = proOnboadringUiMapper.state()
    }
}
