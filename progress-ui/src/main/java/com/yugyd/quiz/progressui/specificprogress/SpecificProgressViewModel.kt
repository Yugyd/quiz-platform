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
import androidx.lifecycle.viewModelScope
import com.yugyd.quiz.commonui.base.BaseViewModel
import com.yugyd.quiz.core.Logger
import com.yugyd.quiz.core.runCatch
import com.yugyd.quiz.domain.controller.RecordController
import com.yugyd.quiz.domain.progress.ProgressInteractor
import com.yugyd.quiz.domain.progress.RecordInteractor
import com.yugyd.quiz.progressui.model.ProgressUiMapper
import com.yugyd.quiz.progressui.specificprogress.SpecificProgressView.Action
import com.yugyd.quiz.progressui.specificprogress.SpecificProgressView.State
import com.yugyd.quiz.progressui.specificprogress.SpecificProgressView.State.NavigationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpecificProgressViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recordInteractor: RecordInteractor,
    private val progressInteractor: ProgressInteractor,
    private val recordController: RecordController,
    private val progressMapper: ProgressUiMapper,
    logger: Logger,
) :
    BaseViewModel<State, Action>(
        logger,
        State(
            payload = SpecificProgressArgs(savedStateHandle).payload,
            isLoading = true,
        )
    ),
    RecordController.Listener {

    init {
        recordController.subscribe(this)
        initData()
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
        screenState = screenState.copy(isLoading = true)

        viewModelScope.launch {
            runCatch(
                block = {
                    val items = progressInteractor.getSpecificProgressItems(
                        screenState.payload.themeId,
                    )
                    val isProgress = progressInteractor.isRecord(items)
                    processItems(items, isProgress)
                },
                catch = ::processItemsError
            )
        }
    }

    private fun processItems(items: List<Any>, isRecord: Boolean) {
        screenState = screenState.copy(
            models = items,
            themeTitle = screenState.payload.themeTitle,
            resetState = if (isRecord) State.ResetState.VISIBLE else State.ResetState.HIDE,
            items = progressMapper.map(items),
            isWarning = items.isEmpty(),
            isLoading = false
        )
    }

    private fun processItemsError(error: Throwable) {
        screenState = screenState.copy(
            isLoading = false,
            isWarning = true,
            showErrorMessage = true,
        )
        processError(error)
    }

    private fun resetProgress() {
        viewModelScope.launch {
            runCatch(
                block = {
                    recordInteractor.resetRecord(screenState.payload.themeId)
                },
                catch = ::processError,
            )
        }
    }
}
