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

package com.yugyd.quiz.gameui.errorlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yugyd.quiz.commonui.base.BaseViewModel
import com.yugyd.quiz.domain.interactor.error.ErrorInteractor
import com.yugyd.quiz.domain.model.errorlist.ErrorModel
import com.yugyd.quiz.domain.repository.Logger
import com.yugyd.quiz.domain.utils.runCatch
import com.yugyd.quiz.gameui.errorlist.ErrorListView.Action
import com.yugyd.quiz.gameui.errorlist.ErrorListView.State
import com.yugyd.quiz.gameui.errorlist.ErrorListView.State.NavigationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ErrorListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val errorInteractor: ErrorInteractor,
    logger: Logger
) :
    BaseViewModel<State, Action>(
        logger = logger,
        initialState = State(
            payload = ErrorListArgs(savedStateHandle).errorListPayload,
            isLoading = true
        )
    ) {

    init {
        loadData()
    }

    override fun handleAction(action: Action) {
        when (action) {
            Action.OnBackClicked -> onBackClicked()
            is Action.OnErrorClicked -> onErrorClicked(action.item)
            Action.OnSnackbarDismissed -> {
                screenState = screenState.copy(showErrorMessage = false)
            }

            Action.OnNavigationHandled -> {
                screenState = screenState.copy(navigationState = null)
            }
        }
    }

    private fun onBackClicked() {
        screenState = screenState.copy(navigationState = NavigationState.Back)
    }

    private fun onErrorClicked(item: ErrorModel) {
        screenState = screenState.copy(
            navigationState = NavigationState.NavigateToExternalBrowser(item.queryLink)
        )
    }

    private fun loadData() {
        viewModelScope.launch {
            runCatch(
                block = {
                    val state = errorInteractor
                        .getErrorsModels(screenState.payload.errorQuestIds)
                    processData(state)
                },
                catch = ::processDataError
            )
        }
    }

    private fun processData(models: List<ErrorModel>) {
        screenState = screenState.copy(
            items = models,
            isWarning = models.isEmpty(),
            isLoading = false,
        )
    }

    private fun processDataError(error: Throwable) {
        screenState = screenState.copy(
            isLoading = false,
            isWarning = true,
            showErrorMessage = true,
        )
        processError(error)
    }
}
