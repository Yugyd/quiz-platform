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

package com.yugyd.quiz.ui.end.gameend

import androidx.lifecycle.SavedStateHandle
import com.yugyd.quiz.commonui.base.BaseViewModel
import com.yugyd.quiz.core.Logger
import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import com.yugyd.quiz.core.runCatch
import com.yugyd.quiz.domain.api.model.Mode.ARCADE
import com.yugyd.quiz.domain.api.model.Mode.ERROR
import com.yugyd.quiz.domain.api.model.Mode.FAVORITE
import com.yugyd.quiz.domain.api.model.Mode.MARATHON
import com.yugyd.quiz.domain.api.model.Mode.NONE
import com.yugyd.quiz.domain.api.model.Mode.TRAIN
import com.yugyd.quiz.domain.api.model.payload.GameEndPayload
import com.yugyd.quiz.domain.api.payload.ErrorListPayload
import com.yugyd.quiz.domain.theme.ThemeInteractor
import com.yugyd.quiz.featuretoggle.domain.FeatureManager
import com.yugyd.quiz.featuretoggle.domain.model.FeatureToggle
import com.yugyd.quiz.ui.end.EndArgs
import com.yugyd.quiz.ui.end.gameend.GameEndView.Action
import com.yugyd.quiz.ui.end.gameend.GameEndView.State
import com.yugyd.quiz.ui.end.gameend.GameEndView.State.NavigationState
import com.yugyd.quiz.ui.end.gameend.model.GameEndUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class GameEndViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val themeInteractor: ThemeInteractor,
    private val gameEndUiMapper: GameEndUiMapper,
    private val featureManager: FeatureManager,
    logger: Logger,
    dispatchersProvider: DispatchersProvider,
) :
    BaseViewModel<State, Action>(
        logger = logger,
        dispatchersProvider = dispatchersProvider,
        initialState = State(
            payload = EndArgs(savedStateHandle).payload,
        )
    ) {

    init {
        loadData()
    }

    override fun handleAction(action: Action) {
        when (action) {
            Action.OnBackClicked -> onBackClicked()
            Action.OnNewGameClicked -> onNewGameClicked()
            Action.OnShowErrorsClicked -> onShowErrorsClicked()
            Action.OnNavigationHandled -> {
                screenState = screenState.copy(navigationState = null)
            }
        }
    }

    private fun onBackClicked() = navigateToBack()

    private fun onNewGameClicked() = navigateToBack()

    private fun onShowErrorsClicked() {
        val payload = ErrorListPayload(errorQuestIds = screenState.payload.errorQuestIds)
        screenState = screenState.copy(
            navigationState = NavigationState.NavigateToErrorList(payload),
        )
    }

    private fun loadData() {
        vmScopeErrorHandled.launch {
            screenState = screenState.copy(isLoading = true)

            val isAdFeatureEnabled = featureManager.isFeatureEnabled(FeatureToggle.AD)

            runCatch(
                block = {
                    val themeTitle = getThemeTitle(screenState.payload)
                    val state = gameEndUiMapper.map(
                        payload = screenState.payload,
                        themeTitle = themeTitle,
                        isAdFeatureEnabled = isAdFeatureEnabled
                    )
                    processData(state)
                },
                catch = {
                    screenState = screenState.copy(isLoading = false)
                    processError(it)
                }
            )

            if (isAdFeatureEnabled) {
                // TODO Implement logic with ads - https://yudyd.atlassian.net/browse/QUIZ-203
            }
        }
    }

    private suspend fun getThemeTitle(payload: GameEndPayload) = when (payload.mode) {
        ARCADE, MARATHON, TRAIN -> {
            payload.themeId?.let { themeId ->
                themeInteractor.getTheme(themeId).name
            }.orEmpty()
        }

        ERROR, FAVORITE, NONE -> ""
    }


    private fun processData(state: State) {
        screenState = state
    }

    private fun navigateToBack() {
        if (
            screenState.isAdFeatureEnabled &&
            !screenState.payload.isRewardedSuccess &&
            !screenState.payload.isBlockedInterstitial
        ) {
            // TODO Implement logic with ads - https://yudyd.atlassian.net/browse/QUIZ-203
        }

        screenState = screenState.copy(navigationState = NavigationState.Back)
    }
}
