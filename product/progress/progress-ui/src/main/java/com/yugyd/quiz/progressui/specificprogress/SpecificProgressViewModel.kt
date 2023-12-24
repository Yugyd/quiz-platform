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

package com.yugyd.quiz.progressui.specificprogress

import androidx.lifecycle.SavedStateHandle
import com.yugyd.quiz.commonui.base.BaseViewModel
import com.yugyd.quiz.core.Logger
import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import com.yugyd.quiz.core.runCatch
import com.yugyd.quiz.domain.content.ContentInteractor
import com.yugyd.quiz.domain.content.models.ContentResult
import com.yugyd.quiz.domain.controller.RecordController
import com.yugyd.quiz.domain.progress.ProgressInteractor
import com.yugyd.quiz.domain.progress.RecordInteractor
import com.yugyd.quiz.progressui.model.ProgressUiMapper
import com.yugyd.quiz.progressui.specificprogress.SpecificProgressView.Action
import com.yugyd.quiz.progressui.specificprogress.SpecificProgressView.State
import com.yugyd.quiz.progressui.specificprogress.SpecificProgressView.State.NavigationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpecificProgressViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recordInteractor: RecordInteractor,
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
        initialState = State(
            payload = SpecificProgressArgs(savedStateHandle).payload,
        )
    ),
    RecordController.Listener {

    private var loadContentJob: Job? = null

    init {
        recordController.subscribe(this)

        initData()

        loadContent()
    }

    override fun onCleared() {
        recordController.unsubscribe(this)
        super.onCleared()
    }

    override fun onRecordUpdate() = initData()

    override fun handleAction(action: Action) {
        when (action) {
            Action.OnBackPressed -> onBackPressed()
            Action.OnResetProgress -> onResetProgress()
            Action.OnResetProgressAccepted -> onResetProgressAccepted()
            Action.OnSnackbarDismissed -> {
                screenState = screenState.copy(showErrorMessage = false)
            }

            Action.OnResetDialogDismissed -> {
                screenState = screenState.copy(showResetDialog = false)
            }

            Action.OnNavigationHandled -> {
                screenState = screenState.copy(navigationState = null)
            }
        }
    }

    private fun onBackPressed() {
        screenState = screenState.copy(navigationState = NavigationState.Back)
    }

    private fun onResetProgress() {
        screenState = screenState.copy(showResetDialog = true)
    }

    private fun onResetProgressAccepted() = resetProgress()

    private fun initData() {
        screenState = screenState.copy(
            isLoading = true,
            isWarning = false,
            resetState = State.ResetState.HIDE,
            items = emptyList(),
        )

        vmScopeErrorHandled.launch {
            runCatch(
                block = {
                    val items = progressInteractor.getSpecificProgressItems(
                        screenState.payload.themeId,
                    )
                    val isProgress = progressInteractor.isRecord(items)
                    processItems(items, isProgress)
                },
                catch = ::processItemsError,
            )
        }
    }

    private fun processItems(items: List<Any>, isRecord: Boolean) {
        screenState = screenState.copy(
            models = items,
            themeTitle = screenState.payload.themeTitle,
            resetState = if (isRecord) State.ResetState.VISIBLE else State.ResetState.HIDE,
            items = progressMapper.map(items),
            isWarning = false,
            isLoading = false
        )
    }

    private fun processItemsError(error: Throwable) {
        screenState = screenState.copy(
            isLoading = false,
            isWarning = true,
            resetState = State.ResetState.HIDE,
            items = emptyList(),
            showErrorMessage = true,
        )
        processError(error)
    }

    private fun loadContent() {
        loadContentJob?.cancel()
        loadContentJob = vmScopeErrorHandled.launch {
            contentInteractor
                .subscribeToSelectedContent()
                .map {
                    contentInteractor.isResetNavigation(
                        oldModel = screenState.contentModel,
                        newModel = it,
                    )
                }
                .catch {
                    processItemsError(it)
                }
                .collect(::processContentResetEvent)
        }
    }

    private fun processContentResetEvent(model: ContentResult) {
        val navigationState = if (model.isBack) {
            NavigationState.Back
        } else {
            screenState.navigationState
        }

        screenState = screenState.copy(
            contentModel = model.newModel,
            navigationState = navigationState,
        )
    }

    private fun resetProgress() {
        vmScopeErrorHandled.launch {
            runCatch(
                block = {
                    recordInteractor.resetRecord(screenState.payload.themeId)
                },
                catch = ::processError,
            )
        }
    }
}
