/*
 * Copyright 2024 Roman Likhachev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yugyd.quiz.ui.aisettings

import com.yugyd.quiz.ai.connection.api.model.AiConnectionModel
import com.yugyd.quiz.commonui.base.BaseViewModel
import com.yugyd.quiz.core.Logger
import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import com.yugyd.quiz.core.result
import com.yugyd.quiz.domain.aiconnection.AiConnectionInteractor
import com.yugyd.quiz.domain.aiconnection.model.AiTermCache
import com.yugyd.quiz.ui.aisettings.AiSettingsView.Action
import com.yugyd.quiz.ui.aisettings.AiSettingsView.State
import com.yugyd.quiz.ui.aisettings.AiSettingsView.State.NavigationState
import com.yugyd.quiz.ui.aisettings.model.AiConnectionUiModel
import com.yugyd.quiz.ui.aisettings.model.AiSettingsUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AiSettingsViewModel @Inject constructor(
    private val aiConnectionInteractor: AiConnectionInteractor,
    private val aiSettingsUiMapper: AiSettingsUiMapper,
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

        loadAiTermCache()

        loadAiConnection()
    }

    override fun handleAction(action: Action) {
        when (action) {
            Action.OnBackClicked -> {
                screenState = screenState.copy(navigationState = NavigationState.Back)
            }

            is Action.OnAiEnabledChecked -> {
                onAiEnabledChecked(action.isAiEnabled)
            }

            is Action.OnAiConnectionClicked -> onAiConnectionClicked(action.item)

            Action.OnAddAiConnectionClicked -> navigateToAiConnectionDetails(item = null)

            is Action.OnEditAiConnectionClicked -> navigateToAiConnectionDetails(action.item)

            is Action.OnDirectAiConnectionChecked -> {
                onDirectAiConnectionChecked(action.isDirectConnectionEnabled)
            }

            Action.OnUpdateTermClicked -> onUpdateTermClicked()

            Action.OnInfoClicked -> onInfoClicked()

            Action.OnInfoDialogDismissed -> {
                screenState = screenState.copy(isInfoDialogVisible = false)
            }

            Action.OnNavigationHandled -> {
                screenState = screenState.copy(navigationState = null)
            }

            Action.OnSnackbarDismissed -> {
                screenState = screenState.copy(showErrorMessage = false)
            }

            is Action.OnTermOfCacheOptionSelected -> {
                screenState = screenState.copy(
                    newTermOfCacheValue = action.option.toInt(),
                )
            }

            Action.OnTermOfCacheDialogDismissed -> {
                screenState = screenState.copy(
                    isTermOfCacheDialogVisible = false,
                    newTermOfCacheValue = null
                )
            }

            Action.OnTermOfCacheSaveClicked -> onTermOfCacheSaveClicked()

            Action.OnAiUsagePolicyClicked -> onAiUsagePolicyCliecked()
        }
    }

    private fun loadAiTermCache() {
        vmScopeErrorHandled.launch {
            aiConnectionInteractor
                .subscribeAiTermCache()
                .catch {
                    processError(it)
                }
                .collect {
                    processAiTermCache(it)
                }
        }
    }

    private fun processAiTermCache(aiTermCache: AiTermCache) {
        screenState = screenState.copy(termOfCacheValue = aiTermCache.daysCount)
    }

    private fun loadAiConnection() {
        vmScopeErrorHandled.launch {
            aiConnectionInteractor
                .subscribeCurrentAiConnection()
                .catch {
                    processError(it)
                }
                .collect {
                    loadData()
                }
        }
    }

    private fun loadData() {
        vmScopeErrorHandled.launch {
            screenState = screenState.copy(
                isLoading = true,
                isWarning = false,
                items = emptyList(),
                termOfCacheOptions = emptyList(),
            )

            result {
                val aiConnections = aiConnectionInteractor.getAllAiConnections()
                val currentAiConnection = aiConnectionInteractor.getCurrentAiConnection()
                val aiTerms = aiConnectionInteractor.getAiTermCaches()
                val isAiEnabled = aiConnectionInteractor
                    .subscribeToAiEnabled()
                    .firstOrNull() ?: false
                val isDirectConnection = aiConnectionInteractor
                    .subscribeToDirectConnection()
                    .firstOrNull() ?: false
                val aiPrivacyUrl = aiConnectionInteractor.getAiPrivacyUrl()

                LoadDataResult(
                    aiConnections = aiConnections,
                    currentAiConnection = currentAiConnection,
                    aiTerms = aiTerms,
                    isAiEnabled = isAiEnabled,
                    isDirectConnection = isDirectConnection,
                    aiPrivacyUrl = aiPrivacyUrl,
                )
            }
                .onFailure {
                    processDataError(it)
                }
                .onSuccess {
                    processData(it)
                }
        }
    }

    private fun processData(dataResult: LoadDataResult) {
        screenState = screenState.copy(
            isLoading = false,
            isWarning = false,
            items = aiSettingsUiMapper.map(dataResult.aiConnections),
            isAddConnectionVisible = dataResult.aiConnections.isEmpty(),
            termOfCacheOptions = aiSettingsUiMapper.mapAiTerms(dataResult.aiTerms),
            isAiEnabled = dataResult.isAiEnabled,
            isDirectConnectionEnabled = dataResult.isDirectConnection,
            aiPrivacyUrl = dataResult.aiPrivacyUrl
        )
    }

    private fun processDataError(error: Throwable) {
        screenState = screenState.copy(
            isLoading = false,
            isWarning = true,
            items = emptyList(),
            isAddConnectionVisible = true,
            termOfCacheOptions = emptyList(),
            aiPrivacyUrl = null,
            showErrorMessage = true,
        )
        processError(error)
    }

    private fun navigateToAiConnectionDetails(item: AiConnectionUiModel?) {
        val navigationState = if (item != null) {
            NavigationState.NavigateToAiConnectionDetails(id = item.id)
        } else {
            NavigationState.NavigateToAiConnectionDetails(id = null)
        }
        screenState = screenState.copy(navigationState = navigationState)
    }

    private fun onAiConnectionClicked(item: AiConnectionUiModel) {
        if (item.isActive) {
            navigateToAiConnectionDetails(item)
        } else {
            activeAiConnection(id = item.id)
        }
    }

    private fun activeAiConnection(id: String) {
        vmScopeErrorHandled.launch {
            result {
                aiConnectionInteractor.activeAiConnection(id = id)
            }
                .onFailure {
                    processError(it)
                }
                .onSuccess {
                    loadData()
                }
        }
    }

    private fun onUpdateTermClicked() {
        screenState = screenState.copy(
            isTermOfCacheDialogVisible = true,
            newTermOfCacheValue = screenState.termOfCacheValue,
        )
    }

    private fun onInfoClicked() {
        screenState = screenState.copy(isInfoDialogVisible = true)
    }

    private fun onTermOfCacheSaveClicked() {
        vmScopeErrorHandled.launch {
            result {
                val aiTermCache = AiTermCache.fromValue(screenState.newTermOfCacheValue!!)
                aiConnectionInteractor.addAiTermCache(aiTermCache)
            }
                .onFailure {
                    processError(it)

                    screenState = screenState.copy(
                        isTermOfCacheDialogVisible = false,
                        newTermOfCacheValue = null,
                    )
                }
                .onSuccess {
                    screenState = screenState.copy(
                        isTermOfCacheDialogVisible = false,
                        newTermOfCacheValue = null,
                    )
                }
        }
    }

    private fun onDirectAiConnectionChecked(isDirectConnection: Boolean) {
        vmScopeErrorHandled.launch {
            screenState = screenState.copy(isDirectConnectionEnabled = isDirectConnection)

            result {
                aiConnectionInteractor.setDirectConnection(isDirectConnection)
            }
                .onFailure {
                    processError(it)
                }
                .onSuccess {
                    processDirectAiConnection()
                }
        }
    }

    private suspend fun processDirectAiConnection() {
        val isDirectConnectionEnabled = aiConnectionInteractor
            .subscribeToDirectConnection()
            .firstOrNull() ?: false

        screenState = screenState.copy(
            isDirectConnectionEnabled = isDirectConnectionEnabled,
        )
    }

    private fun onAiEnabledChecked(isAiEnabled: Boolean) {
        vmScopeErrorHandled.launch {
            screenState = screenState.copy(isAiEnabled = isAiEnabled)

            result {
                aiConnectionInteractor.setAiEnabled(isAiEnabled)
            }
                .onFailure {
                    processError(it)
                }
                .onSuccess {
                    processIsAiEnabled()
                }
        }
    }

    private suspend fun processIsAiEnabled() {
        val isAiEnabled = aiConnectionInteractor
            .subscribeToAiEnabled()
            .firstOrNull() ?: false

        screenState = screenState.copy(isAiEnabled = isAiEnabled)
    }

    private fun onAiUsagePolicyCliecked() {
        val aiPrivacyUrl = screenState.aiPrivacyUrl
        if (aiPrivacyUrl.isNullOrEmpty()) {
            return
        }

        screenState = screenState.copy(
            navigationState = NavigationState.NavigateToAiUsagePolicy(aiPrivacyUrl = aiPrivacyUrl),
        )
    }

    private data class LoadDataResult(
        val aiConnections: List<AiConnectionModel>,
        val currentAiConnection: AiConnectionModel?,
        val aiTerms: List<AiTermCache>,
        val isAiEnabled: Boolean,
        val isDirectConnection: Boolean,
        val aiPrivacyUrl: String?,
    )
}
