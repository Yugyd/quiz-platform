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

package com.yugyd.quiz.ui.searchcourses

import com.yugyd.quiz.commonui.base.BaseViewModel
import com.yugyd.quiz.core.Logger
import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import com.yugyd.quiz.core.runCatch
import com.yugyd.quiz.domain.courses.SearchCourseInteractor
import com.yugyd.quiz.domain.courses.model.CourseModel
import com.yugyd.quiz.ui.searchcourses.SearchCourseView.Action
import com.yugyd.quiz.ui.searchcourses.SearchCourseView.State
import com.yugyd.quiz.ui.searchcourses.SearchCourseView.State.NavigationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SearchCourseViewModel @Inject constructor(
    private val searchCourseInteractor: SearchCourseInteractor,
    logger: Logger,
    dispatchersProvider: DispatchersProvider,
) :
    BaseViewModel<State, Action>(
        logger = logger,
        dispatchersProvider = dispatchersProvider,
        initialState = State(),
    ) {

    init {
        loadSearchSuggestions()
        loadData()
    }

    override fun handleAction(action: Action) {
        when (action) {
            is Action.OnCourseClicked -> onCourseClicked(action.item)

            Action.OnSnackbarDismissed -> {
                screenState = screenState.copy(showErrorMessage = false)
            }

            Action.OnNavigationHandled -> {
                screenState = screenState.copy(navigationState = null)
            }

            is Action.OnSearchClicked -> onSearchClicked(action.query)

            Action.OnBackPressed -> {
                screenState = screenState.copy(navigationState = NavigationState.Back)
            }

            is Action.OnSearchQueryChanged -> onSearchQueryChanged(action.query)
        }
    }

    private fun loadSearchSuggestions() {
        vmScopeErrorHandled.launch {
            runCatch(
                block = {
                    getAndUpdateSuggestions()
                },
                catch = ::processError,
            )
        }
    }

    private suspend fun getAndUpdateSuggestions() {
        val searchSuggestions = searchCourseInteractor.getLatestQueries(
            limit = OPTIMAL_SEARCH_SUGGESTIONS_LIMIT,
        )

        screenState = screenState.copy(
            searchState = screenState.searchState.copy(
                suggestions = searchSuggestions,
            ),
        )
    }

    private fun onCourseClicked(item: CourseModel) {
        val navigationState = if (item.isDetail) {
            NavigationState.NavigateToCourseDetail(
                id = item.id,
                title = item.name,
            )
        } else {
            NavigationState.NavigateToSubCourse(
                id = item.id,
                title = item.name,
                isHideContinueCourseBanner = false,
            )
        }
        screenState = screenState.copy(navigationState = navigationState)
    }

    private fun loadData() {
        val query = screenState.searchState.query
        if (query.isBlank()) {
            return
        }

        vmScopeErrorHandled.launch {
            val searchState = screenState.searchState.copy(
                currentRequestQuery = query,
                isEnabled = false,
            )
            screenState = screenState.copy(
                isLoading = true,
                isWarning = false,
                items = emptyList(),
                searchState = searchState,
            )

            delay(5000L)

            runCatch(
                block = {
                    val foundCourses = searchCourseInteractor.searchCourse(query = query)
                    processData(courses = foundCourses)
                },
                catch = ::processDataError,
            )
        }
    }

    private fun processData(courses: List<CourseModel>) {
        val searchState = screenState.searchState.copy(isEnabled = true)
        screenState = screenState.copy(
            isLoading = false,
            isWarning = false,
            items = courses,
            searchState = searchState,
        )
    }

    private fun processDataError(error: Throwable) {
        val searchState = screenState.searchState.copy(isEnabled = false)
        screenState = screenState.copy(
            isLoading = false,
            isWarning = true,
            items = emptyList(),
            showErrorMessage = true,
            searchState = searchState,
        )
        processError(error)
    }

    private fun onSearchClicked(query: String?) {
        if (query != null) {
            screenState = screenState.copy(
                searchState = screenState.searchState.copy(query = query),
            )
        }

        val currentQuery = screenState.searchState.query

        vmScopeErrorHandled.launch {
            runCatch(
                block = {
                    searchCourseInteractor.setQuery(currentQuery)

                    getAndUpdateSuggestions()

                    loadData()
                },
                catch = ::processError,
            )
        }
    }

    private fun onSearchQueryChanged(query: String) {
        val currentRequestQuery = if (query != screenState.searchState.currentRequestQuery) {
            ""
        } else {
            screenState.searchState.currentRequestQuery
        }
        val searchState = screenState.searchState.copy(
            query = query,
            currentRequestQuery = currentRequestQuery,
        )
        screenState = screenState.copy(searchState = searchState)
    }

    private companion object {
        private const val OPTIMAL_SEARCH_SUGGESTIONS_LIMIT = 5
    }
}
