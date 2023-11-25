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

package com.yugyd.quiz.ui.content

import android.net.Uri
import androidx.core.net.toFile
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yugyd.quiz.commonui.base.BaseViewModel
import com.yugyd.quiz.core.Logger
import com.yugyd.quiz.core.runCatch
import com.yugyd.quiz.domain.content.ContentInteractor
import com.yugyd.quiz.domain.content.api.ContentModel
import com.yugyd.quiz.domain.content.exceptions.ContentNotValidException
import com.yugyd.quiz.domain.content.exceptions.ContentVerificationException
import com.yugyd.quiz.ui.content.ContentView.Action
import com.yugyd.quiz.ui.content.ContentView.State
import com.yugyd.quiz.ui.content.ContentView.State.NavigationState
import com.yugyd.quiz.ui.content.ContentView.State.SnackbarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ContentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: ContentInteractor,
    logger: Logger,
) :
    BaseViewModel<State, Action>(
        logger = logger,
        initialState = State(
            isBackEnabled = ContentArgs(savedStateHandle).isBackEnabled,
            isLoading = true,
        )
    ) {

    init {
        loadData()
    }

    override fun handleAction(action: Action) {
        when (action) {
            Action.OnBackClicked -> onBackClicked()

            is Action.OnItemClicked -> onItemClicked(action.item)

            is Action.OnDeleteClicked -> onDeleteClicked(action.item)

            Action.OnSnackbarDismissed -> {
                screenState = screenState.copy(snackbarState = null)
            }

            Action.OnNavigationHandled -> {
                screenState = screenState.copy(navigationState = null)
            }

            Action.OnOpenFileClicked -> {
                screenState = screenState.copy(startFileProvider = true)
            }

            is Action.OnDocumentResult -> {
                onDocumentResult(action.uri)
            }

            Action.OnStartFileProviderHandled -> {
                screenState = screenState.copy(startFileProvider = false)
            }
        }
    }

    private fun onBackClicked() {
        screenState = screenState.copy(navigationState = NavigationState.Back)
    }

    private fun onItemClicked(item: ContentModel) {
        val selectedItem = requireNotNull(screenState.items).first { it.isChecked }
        if (item == selectedItem) {
            return
        }

        viewModelScope.launch {
            runCatch(
                block = {
                    val result = interactor.selectContent(
                        oldModel = selectedItem,
                        newModel = item,
                    )
                    processOnItemClicked(result)
                },
                catch = ::processOnItemClickedError,
            )
        }
    }

    private fun processOnItemClicked(isAdded: Boolean) {
        screenState = if (isAdded) {
            screenState.copy(navigationState = NavigationState.Back)
        } else {
            screenState.copy(snackbarState = SnackbarState.NotAddedContentIsExists)
        }
    }

    private fun processOnItemClickedError(error: Throwable) {
        processError(error)

        screenState = when (error) {
            is ContentVerificationException -> {
                screenState.copy(snackbarState = SnackbarState.VerifyError(error))
            }

            is ContentNotValidException -> {
                screenState.copy(snackbarState = SnackbarState.NotSelectAndDelete)
            }

            else -> {
                screenState.copy(snackbarState = SnackbarState.SelectIsFailed)
            }
        }
    }

    private fun onDeleteClicked(item: ContentModel) {
        viewModelScope.launch {
            runCatch(
                block = {
                    interactor.deleteContent(item.id)
                },
                catch = {
                    processError(it)

                    screenState = screenState.copy(snackbarState = SnackbarState.DeleteIsFailed)
                }
            )
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            runCatch(
                block = {
                    val state = interactor.getContents()
                    processData(state)
                },
                catch = ::processDataError,
            )
        }
    }

    private fun processData(models: List<ContentModel>) {
        screenState = screenState.copy(
            items = models,
            isWarning = false,
            isLoading = false,
        )
    }

    private fun processDataError(error: Throwable) {
        processError(error)
        screenState = screenState.copy(
            items = null,
            isLoading = false,
            isWarning = true,
        )
    }

    private fun onDocumentResult(uri: Uri?) {
        if (uri == null) {
            screenState = screenState.copy(snackbarState = SnackbarState.UriIsNull)
            return
        }

        viewModelScope.launch {
            runCatch(
                block = {
                    val selectedItem = requireNotNull(screenState.items).first { it.isChecked }

                    val fileName = uri.toFile().nameWithoutExtension.replaceFirstChar {
                        if (it.isLowerCase()) {
                            it.titlecase(Locale.getDefault())
                        } else {
                            it.toString()
                        }
                    }

                    val isAdded = interactor.addContent(
                        oldModel = selectedItem,
                        // TODO Add feature to set a name for content
                        contentName = fileName,
                        uri = uri,
                    )

                    processOnItemClicked(isAdded)
                },
                catch = ::processOnDocumentResultError,
            )
        }
    }

    private fun processOnDocumentResultError(error: Throwable) {
        processError(error)

        screenState = when (error) {
            is ContentVerificationException -> {
                screenState.copy(snackbarState = SnackbarState.VerifyError(error))
            }

            else -> screenState.copy(snackbarState = SnackbarState.AddIsFailed)
        }
    }
}
