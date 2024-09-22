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

package com.yugyd.quiz.ui.tasks

import com.yugyd.quiz.commonui.base.BaseViewModel
import com.yugyd.quiz.core.Logger
import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import com.yugyd.quiz.core.runCatch
import com.yugyd.quiz.domain.api.model.tasks.TaskModel
import com.yugyd.quiz.domain.tasks.TasksInteractor
import com.yugyd.quiz.ui.tasks.TasksView.Action
import com.yugyd.quiz.ui.tasks.TasksView.State
import com.yugyd.quiz.ui.tasks.TasksView.State.NavigationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TasksViewModel @Inject constructor(
    private val tasksInteractor: TasksInteractor,
    logger: Logger,
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
            Action.OnBackClicked -> onBackClicked()
            is Action.OnTaskClicked -> onTaskClicked(action.item)
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

    private fun onTaskClicked(item: TaskModel) {
        screenState = if (item.queryLink.isNotEmpty()) {
            screenState.copy(
                navigationState = NavigationState.NavigateToExternalBrowser(item.queryLink),
            )
        } else {
            screenState.copy(showErrorMessage = true)
        }
    }

    private fun loadData() {
        vmScopeErrorHandled.launch {
            screenState = screenState.copy(
                isLoading = true,
                isWarning = false,
                items = emptyList(),
            )

            runCatch(
                block = {
                    val state = tasksInteractor.getTaskModels()
                    processData(state)
                },
                catch = ::processDataError,
            )
        }
    }

    private fun processData(models: List<TaskModel>) {
        screenState = screenState.copy(
            isLoading = false,
            isWarning = false,
            items = models,
        )
    }

    private fun processDataError(error: Throwable) {
        screenState = screenState.copy(
            isLoading = false,
            isWarning = true,
            items = emptyList(),
            showErrorMessage = true,
        )
        processError(error)
    }
}
