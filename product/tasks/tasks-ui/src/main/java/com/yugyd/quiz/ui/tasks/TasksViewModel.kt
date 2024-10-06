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
import com.yugyd.quiz.domain.errors.ErrorInteractor
import com.yugyd.quiz.domain.favorites.FavoritesInteractor
import com.yugyd.quiz.domain.tasks.FilterInteractor
import com.yugyd.quiz.domain.tasks.TasksInteractor
import com.yugyd.quiz.domain.tasks.model.FilterModel
import com.yugyd.quiz.domain.tasks.model.FilterType
import com.yugyd.quiz.domain.tasks.model.TaskModel
import com.yugyd.quiz.ui.tasks.TasksView.Action
import com.yugyd.quiz.ui.tasks.TasksView.State
import com.yugyd.quiz.ui.tasks.TasksView.State.FilterUiModel
import com.yugyd.quiz.ui.tasks.TasksView.State.NavigationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TasksViewModel @Inject constructor(
    private val tasksInteractor: TasksInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val filterInteractor: FilterInteractor,
    private val errorsInteractor: ErrorInteractor,
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

            is Action.OnFavoriteClicked -> onFavoriteClicked(action.item)

            Action.OnShowFilterClicked -> {
                screenState = screenState.copy(showFilters = true)
            }

            Action.OnFiltersDismissed -> {
                screenState = screenState.copy(showFilters = false)
            }

            is Action.OnFilterClicked -> onFilterClicked(action.item)
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
                allItems = emptyList(),
            )

            runCatch(
                block = {
                    val filters = filterInteractor
                        .getFilters()
                        .map {
                            FilterUiModel(
                                filterModel = FilterModel(
                                    id = it,
                                    isChecked = false,
                                ),
                                titleRes = when (it) {
                                    FilterType.ERRORS -> R.string.tasks_errors
                                    FilterType.FAVORITES -> R.string.tasks_favorites
                                }
                            )
                        }

                    val tasks = tasksInteractor.getTaskModels()
                    val favorites = favoritesInteractor.getFavorites()
                    val taskModels = tasks.map { task ->
                        if (favorites.contains(task.id)) {
                            task.copy(isFavorite = true)
                        } else {
                            task
                        }
                    }
                    processData(filters, taskModels)
                },
                catch = ::processDataError,
            )
        }
    }

    private fun processData(filters: List<FilterUiModel>, models: List<TaskModel>) {
        screenState = screenState.copy(
            isLoading = false,
            isWarning = false,
            allFilters = filters,
            allItems = models,
        )
    }

    private fun processDataError(error: Throwable) {
        screenState = screenState.copy(
            isLoading = false,
            isWarning = true,
            allItems = emptyList(),
            showErrorMessage = true,
        )
        processError(error)
    }

    private fun onFavoriteClicked(item: TaskModel) {
        vmScopeErrorHandled.launch {
            runCatch(
                block = {
                    screenState = screenState.copy(
                        allItems = screenState.allItems.map { task ->
                            if (task.id == item.id) {
                                task.copy(isFavorite = task.isFavorite.not())
                            } else {
                                task
                            }
                        }
                    )

                    val currentTask = screenState.allItems.first { it.id == item.id }
                    favoritesInteractor.updateTask(
                        id = currentTask.id,
                        isFavorite = currentTask.isFavorite,
                    )
                },
                catch = {
                    processError(it)
                    screenState = screenState.copy(showErrorMessage = true)
                }
            )
        }
    }

    private fun onFilterClicked(selectedFilter: FilterUiModel) {
        screenState = screenState.copy(
            allFilters = screenState.allFilters.map { filter ->
                if (filter.filterModel.id == selectedFilter.filterModel.id) {
                    filter.copy(
                        filterModel = filter.filterModel.copy(
                            isChecked = selectedFilter.filterModel.isChecked.not(),
                        ),
                    )
                } else {
                    filter
                }
            }
        )
        applyFilters()
    }

    private fun applyFilters() {
        vmScopeErrorHandled.launch {
            val favoriteIds = favoritesInteractor.getFavorites()
            val errorIds = errorsInteractor.getErrors()
            val newShowItemIds = filterInteractor.applyFilters(
                filters = screenState.allFilters.map(FilterUiModel::filterModel),
                allTaskIds = screenState.allItems.map(TaskModel::id),
                errorsIds = errorIds,
                favorites = favoriteIds,
            )
            val newShowItems = screenState.allItems.map { task ->
                task.copy(isShow = newShowItemIds.contains(task.id))
            }
            screenState = screenState.copy(allItems = newShowItems)
        }
    }
}
