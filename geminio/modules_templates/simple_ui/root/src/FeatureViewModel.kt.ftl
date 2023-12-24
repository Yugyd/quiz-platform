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

package ${packageName}

import androidx.lifecycle.SavedStateHandle
import ${generalPackage}.commonui.base.BaseViewModel
import ${corePackage}.Logger
import ${corePackage}.runCatch
import ${domainPackage}.model.${modelName}
import ${domainPackage}.${interactorName}
import ${packageName}.${viewName}.Action
import ${packageName}.${viewName}.State
import ${packageName}.${viewName}.State.NavigationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ${viewModelName} @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: ${interactorName},
    logger: Logger,
) :
    BaseViewModel<State, Action>(
        logger = logger,
        initialState = State(
            payload = ${argsName}(savedStateHandle).payload,
            isLoading = true
        )
    ) {

    init {
        loadData()
    }

    override fun handleAction(action: Action) {
        when (action) {
            Action.OnBackClicked -> onBackClicked()

            is Action.OnItemClicked -> onItemClicked(action.item)

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

    private fun onItemClicked(item: ${modelName}) {
        screenState = screenState.copy(
            navigationState = NavigationState.NavigateToExternalBrowser(item.id)
        )
    }

    private fun loadData() {
        vmScopeErrorHandled.launch {
            runCatch(
                block = {
                    val state = interactor.getData(screenState.payload.id)
                    processData(state)
                },
                catch = ::processDataError
            )
        }
    }

    private fun processData(model: ${modelName}) {
        screenState = screenState.copy(
            item = model,
            isWarning = model == null,
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
