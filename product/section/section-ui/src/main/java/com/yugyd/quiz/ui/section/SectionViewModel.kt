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

package com.yugyd.quiz.ui.section

import androidx.lifecycle.SavedStateHandle
import com.yugyd.quiz.commonui.base.BaseViewModel
import com.yugyd.quiz.core.Logger
import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import com.yugyd.quiz.core.runCatch
import com.yugyd.quiz.domain.api.model.Mode
import com.yugyd.quiz.domain.api.model.payload.GamePayload
import com.yugyd.quiz.domain.content.ContentInteractor
import com.yugyd.quiz.domain.content.models.ContentResult
import com.yugyd.quiz.domain.controller.SectionController
import com.yugyd.quiz.domain.section.SectionInteractor
import com.yugyd.quiz.domain.section.model.Section
import com.yugyd.quiz.ui.section.SectionView.Action
import com.yugyd.quiz.ui.section.SectionView.State
import com.yugyd.quiz.ui.section.SectionView.State.NavigationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SectionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sectionInteractor: SectionInteractor,
    private val contentInteractor: ContentInteractor,
    private val sectionController: SectionController,
    logger: Logger,
    dispatchersProvider: DispatchersProvider,
) :
    BaseViewModel<State, Action>(
        logger = logger,
        dispatchersProvider = dispatchersProvider,
        initialState = State(
            payload = SectionsArgs(savedStateHandle).payload,
        )
    ),
    SectionController.Listener {

    private var loadContentJob: Job? = null

    init {
        sectionController.subscribe(this)
        initData()
        loadContent()
    }

    override fun onCleared() {
        sectionController.unsubscribe(this)
        super.onCleared()
    }

    override fun onSectionUpdate() {
        initData()
    }

    override fun handleAction(action: Action) {
        when (action) {
            Action.OnBackPressed -> onBackPressed()
            is Action.OnSectionClicked -> onSectionClicked(action.item)
            Action.OnSnackbarDismissed -> {
                screenState = screenState.copy(showErrorMessage = false)
            }

            Action.OnNavigationHandled -> {
                screenState = screenState.copy(navigationState = null)
            }
        }
    }

    private fun onBackPressed() {
        screenState = screenState.copy(navigationState = NavigationState.Back)
    }

    private fun onSectionClicked(item: Section) {
        if (item.progressState != com.yugyd.quiz.domain.section.model.ProgressState.LOCKED) {
            navigateToGame(item)
        }
    }

    private fun initData() {
        vmScopeErrorHandled.launch {
            screenState = screenState.copy(
                models = emptyList(),
                isLoading = true,
                isWarning = false,
                themeTitle = screenState.payload.themeTitle,
            )

            runCatch(
                block = {
                    val themes = sectionInteractor.getSections(screenState.payload.themeId)
                    processItems(themes)
                },
                catch = ::processItemsError
            )
        }
    }

    private fun processItems(items: List<Section>) {
        screenState = screenState.copy(
            models = items,
            isWarning = false,
            isLoading = false
        )
    }

    private fun processItemsError(error: Throwable) {
        screenState = screenState.copy(
            isLoading = false,
            isWarning = true,
            showErrorMessage = true
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

    private fun navigateToGame(item: Section) {
        val payload = GamePayload(
            mode = Mode.ARCADE,
            themeId = screenState.payload.themeId,
            sectionId = item.id,
            record = item.point,
        )
        screenState = screenState.copy(
            navigationState = NavigationState.NavigateToGame(payload),
        )
    }
}
