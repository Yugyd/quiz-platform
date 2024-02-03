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

package com.yugyd.quiz.correctui

import com.yugyd.quiz.commonui.base.BaseViewModel
import com.yugyd.quiz.core.Logger
import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import com.yugyd.quiz.core.runCatch
import com.yugyd.quiz.correctui.CorrectView.Action
import com.yugyd.quiz.correctui.CorrectView.State
import com.yugyd.quiz.correctui.CorrectView.State.AvailableMode
import com.yugyd.quiz.correctui.CorrectView.State.NavigationState
import com.yugyd.quiz.domain.api.model.Content
import com.yugyd.quiz.domain.api.model.Mode
import com.yugyd.quiz.domain.api.model.payload.GamePayload
import com.yugyd.quiz.domain.api.repository.ContentSource
import com.yugyd.quiz.domain.content.ContentInteractor
import com.yugyd.quiz.domain.controller.ErrorController
import com.yugyd.quiz.domain.errors.ErrorInteractor
import com.yugyd.quiz.featuretoggle.domain.FeatureManager
import com.yugyd.quiz.featuretoggle.domain.model.FeatureToggle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CorrectViewModel @Inject constructor(
    private val errorInteractor: ErrorInteractor,
    private val errorController: ErrorController,
    private val contentInteractor: ContentInteractor,
    private val contentSource: ContentSource,
    private val featureManager: FeatureManager,
    logger: Logger,
    dispatchersProvider: DispatchersProvider,
) :
    BaseViewModel<State, Action>(
        logger = logger,
        dispatchersProvider = dispatchersProvider,
        initialState = State(),
    ),
    ErrorController.Listener {

    private var loadDataJob: Job? = null

    init {
        errorController.subscribe(this)
        loadData()
    }

    override fun onCleared() {
        errorController.unsubscribe(this)
        super.onCleared()
    }

    override fun onErrorUpdate() = loadData()

    override fun handleAction(action: Action) = when (action) {
        Action.OnStartClicked -> onStartClicked()

        Action.OnSnackbarDismissed -> {
            screenState = screenState.copy(showErrorMessage = false)
        }

        Action.OnNavigationHandled -> {
            screenState = screenState.copy(navigationState = null)
        }
    }

    private fun onStartClicked() {
        val payload = GamePayload(mode = Mode.ERROR)
        screenState = screenState.copy(
            navigationState = NavigationState.NavigateToGame(payload)
        )
    }

    private fun loadData() {
        loadDataJob?.cancel()
        loadDataJob = vmScopeErrorHandled.launch {
            screenState = screenState.copy(
                isLoading = true,
                isWarning = false,
                availableMode = AvailableMode.GAME_BUTTON,
                isHaveErrors = false,
            )

            contentInteractor
                .subscribeToSelectedContent()
                .catch {
                    processDataError(it)
                }
                .collect {
                    processData()
                }
        }
    }

    private suspend fun processData() {
        runCatch(
            block = {
                val isProFeatureEnabled = featureManager.isFeatureEnabled(FeatureToggle.PRO)
                val isHaveErrors = errorInteractor.isHaveErrors()
                processCorrectData(
                    isHaveErrors = isHaveErrors,
                    isProFeatureEnabled = isProFeatureEnabled,
                )
            },
            catch = ::processDataError,
        )
    }

    private fun processDataError(error: Throwable) {
        screenState = screenState.copy(
            isLoading = false,
            isWarning = true,
            showErrorMessage = true,
            availableMode = AvailableMode.GAME_BUTTON,
            isHaveErrors = false,
        )
        processError(error)
    }

    private fun processCorrectData(isHaveErrors: Boolean, isProFeatureEnabled: Boolean) {
        val availableMode = when (contentSource.getContent()) {
            Content.LITE -> {
                if (isProFeatureEnabled) {
                    AvailableMode.PRO_MESSAGE
                } else {
                    AvailableMode.GAME_BUTTON
                }
            }

            Content.PRO -> AvailableMode.GAME_BUTTON
        }

        screenState = screenState.copy(
            availableMode = availableMode,
            isHaveErrors = isHaveErrors,
            isProFeatureEnabled = isProFeatureEnabled,
            isWarning = false,
            isLoading = false,
        )
    }
}
