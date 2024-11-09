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

package com.yugyd.quiz.update

import com.yugyd.quiz.commonui.base.BaseViewModel
import com.yugyd.quiz.core.ContentProvider
import com.yugyd.quiz.core.Logger
import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import com.yugyd.quiz.core.runCatch
import com.yugyd.quiz.featuretoggle.domain.RemoteConfigRepository
import com.yugyd.quiz.update.UpdateView.Action
import com.yugyd.quiz.update.UpdateView.State
import com.yugyd.quiz.update.UpdateView.State.NavigationState
import com.yugyd.quiz.update.UpdateView.State.UpdateConfigUiModel
import com.yugyd.quiz.update.model.UpdateUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class UpdateViewModel @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository,
    private val updateUiMapper: UpdateUiMapper,
    private val contentProvider: ContentProvider,
    private val logger: Logger,
    dispatchersProvider: DispatchersProvider,
) :
    BaseViewModel<State, Action>(
        logger = logger,
        dispatchersProvider = dispatchersProvider,
        initialState = State(),
    ) {

    init {
        loadData()
    }

    override fun handleAction(action: Action) {
        when (action) {
            is Action.OnUpdateClicked -> onUpdateClicked()

            Action.OnNavigationHandled -> {
                screenState = screenState.copy(navigationState = null)
            }
        }
    }

    private fun loadData() {
        vmScopeErrorHandled.launch {
            screenState = screenState.copy(
                isLoading = true,
                updateConfig = UpdateConfigUiModel(),
            )

            runCatch(
                block = {
                    val updateConfig = remoteConfigRepository.fetchUpdateConfig()
                    screenState = updateUiMapper.map(updateConfig)
                },
                catch = ::processDataError,
            )
        }
    }

    private fun processDataError(error: Throwable) {
        screenState = updateUiMapper.makeDefaultState()
        processError(error)
    }

    private fun onUpdateClicked() {
        vmScopeErrorHandled.launch {
            runCatch(
                block = {
                    val link = contentProvider.getUpdateLink()
                    screenState = screenState.copy(
                        navigationState = NavigationState.NavigateToGooglePlay(link),
                    )
                },
                catch = {
                    logger.logError(TAG, it)
                    screenState = screenState.copy(
                        navigationState = NavigationState.NavigateToGooglePlay(),
                    )
                }
            )
        }
    }

    private companion object {
        private const val TAG = "UpdateViewModel"
    }
}
