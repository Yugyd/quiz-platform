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

package com.yugyd.quiz.progressui.progress

import com.yugyd.quiz.commonui.base.BaseViewModel
import com.yugyd.quiz.core.Logger
import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import com.yugyd.quiz.core.runCatch
import com.yugyd.quiz.domain.api.payload.SpecificProgressPayload
import com.yugyd.quiz.domain.content.ContentInteractor
import com.yugyd.quiz.domain.controller.RecordController
import com.yugyd.quiz.domain.progress.ProgressInteractor
import com.yugyd.quiz.progressui.model.ItemProgressUiModel
import com.yugyd.quiz.progressui.model.ProgressUiMapper
import com.yugyd.quiz.progressui.progress.ProgressView.Action
import com.yugyd.quiz.progressui.progress.ProgressView.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ProgressViewModel @Inject constructor(
    private val progressInteractor: ProgressInteractor,
    private val contentInteractor: ContentInteractor,
    private val recordController: RecordController,
    private val progressMapper: ProgressUiMapper,
    logger: Logger,
    dispatchersProvider: DispatchersProvider,
) :
    BaseViewModel<State, Action>(
        logger = logger,
        dispatchersProvider = dispatchersProvider,
        initialState = State(),
    ),
    RecordController.Listener {

    private var loadDataJob: Job? = null

    private var models: List<Any> = emptyList()

    init {
        recordController.subscribe(this)
        loadData()
    }

    override fun onCleared() {
        recordController.unsubscribe(this)
        super.onCleared()
    }

    override fun onRecordUpdate() = loadData()

    override fun handleAction(action: Action) {
        when (action) {
            is Action.OnProgressClicked -> navigateToSpecificProgress(action.item)
            Action.OnSnackbarDismissed -> {
                screenState = screenState.copy(showErrorMessage = false)
            }

            Action.OnNavigationHandled -> {
                screenState = screenState.copy(navigationState = null)
            }
        }
    }

    private fun loadData() {
        screenState = screenState.copy(
            isLoading = true,
            isWarning = false,
            models = emptyList(),
        )

        loadDataJob?.cancel()
        loadDataJob = vmScopeErrorHandled.launch {
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
                val items = progressInteractor.getProgressItems()
                processItems(items)
            },
            catch = ::processDataError,
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

    private fun processItems(items: List<Any>) {
        models = items

        screenState = screenState.copy(
            items = progressMapper.map(items),
            isWarning = false,
            isLoading = false,
        )
    }

    private fun navigateToSpecificProgress(item: ItemProgressUiModel) {
        val payload = SpecificProgressPayload(
            themeId = item.id,
            themeTitle = item.title
        )
        screenState = screenState.copy(
            navigationState = State.NavigationState.NavigateToSpecificProgress(payload),
        )
    }
}
