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

package com.yugyd.quiz.domain.courses

import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
internal class SearchCourseInteractorImpl @Inject constructor(
    private val courseInteractor: CourseInteractor,
    private val searchCourseLocalSource: SearchCourseLocalSource,
    private val dispatcherProvider: DispatchersProvider,
) : SearchCourseInteractor {

    private val searchMutex = Mutex()

    override suspend fun searchCourse(query: String) = withContext(dispatcherProvider.io) {
        // TODO Replace request to the API
        courseInteractor.getCourses().filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true)
        }
    }

    override suspend fun setQuery(query: String) = withContext(dispatcherProvider.io) {
        if (query.isBlank()) {
            return@withContext
        }

        searchMutex.withLock {
            val currentQueries = searchCourseLocalSource.getQueries()
            val newQueries = listOf(query).plus(currentQueries).take(MAX_SEARCH_SUGGESTIONS)
            searchCourseLocalSource.setQueries(newQueries)
        }
    }

    override suspend fun getLatestQueries(limit: Int?) = withContext(dispatcherProvider.io) {
        searchMutex.withLock {
            val queries = searchCourseLocalSource.getQueries()

            if (limit != null) {
                queries.take(limit)
            } else {
                queries
            }
        }
    }

    private companion object {
        private const val MAX_SEARCH_SUGGESTIONS = 10
    }
}
