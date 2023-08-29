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

package com.yugyd.quiz.ui.transition

import androidx.lifecycle.viewModelScope
import com.yugyd.quiz.commonui.base.BaseViewModel
import com.yugyd.quiz.core.Logger
import com.yugyd.quiz.core.runCatch
import com.yugyd.quiz.domain.controller.TransitionController
import com.yugyd.quiz.domain.options.OptionsInteractor
import com.yugyd.quiz.ui.transition.TransitionView.Action
import com.yugyd.quiz.ui.transition.TransitionView.State
import com.yugyd.quiz.ui.transition.TransitionView.State.NavigationState
import com.yugyd.quiz.ui.transition.model.TransitionUiMapper
import com.yugyd.quiz.ui.transition.model.TransitionUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransitionViewModel @Inject constructor(
    private val transitionController: TransitionController,
    private val optionsInteractor: OptionsInteractor,
    private val uiMapper: TransitionUiMapper,
    logger: Logger,
) : BaseViewModel<State, Action>(logger, State(isLoading = true)) {

    init {
        initData()
    }

    override fun handleAction(action: Action) {
        when (action) {
            Action.OnBackPressed -> onBackPressed()
            is Action.OnTransitionClicked -> onTransitionClicked(action.item)
            Action.OnNavigationHandled -> {
                screenState = screenState.copy(navigationState = null)
            }
        }
    }

    private fun onTransitionClicked(item: TransitionUiModel) {
        changeTransition(item.value)
    }

    private fun onBackPressed() {
        screenState = screenState.copy(navigationState = NavigationState.Back)
    }

    private fun initData() {
        screenState = screenState.copy(isLoading = true)

        val transactions = optionsInteractor
            .getTransitions()
            .map(uiMapper::map)
        processItems(transactions)
    }

    private fun processItems(items: List<TransitionUiModel>) {
        val models = items.map {
            if (it.value == optionsInteractor.transition.value) it.copy(isChecked = true)
            else it
        }

        screenState = screenState.copy(
            items = models,
            isLoading = false
        )
    }

    private fun changeTransition(value: Double) {
        viewModelScope.launch {
            runCatch(
                block = {
                    optionsInteractor.setTransition(value)
                    processTransaction()
                },
                catch = ::processError
            )
        }
    }

    private fun processTransaction() {
        transitionController.notifyEvent()
        screenState = screenState.copy(navigationState = NavigationState.Back)
    }
}
