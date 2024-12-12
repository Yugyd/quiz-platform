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

package com.yugyd.quiz.domain.courses.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.yugyd.quiz.domain.courses.SearchCourseLocalSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchCourseLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
) : SearchCourseLocalSource {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = SEARCH_COURSE_FILE_NAME,
    )

    override suspend fun setQueries(queries: List<String>) {
        context.dataStore.edit { settings ->
            settings[SEARCH_QUERIES_KEY] = queries.toSet()
        }
    }

    override suspend fun getQueries(): List<String> {
        val queries = context.dataStore.data
            .map { preferences ->
                preferences[SEARCH_QUERIES_KEY] ?: emptySet()
            }
            .firstOrNull()
        return queries.orEmpty().toList()
    }

    private companion object {
        private const val SEARCH_COURSE_FILE_NAME = "com.yugyd.quiz.searchcourse"
        private val SEARCH_QUERIES_KEY = stringSetPreferencesKey(
            "search_queries_set",
        )
    }
}