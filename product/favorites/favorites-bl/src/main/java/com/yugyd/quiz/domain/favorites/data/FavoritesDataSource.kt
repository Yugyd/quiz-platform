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

package com.yugyd.quiz.domain.favorites.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.yugyd.quiz.domain.favorites.FavoritesSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
) : FavoritesSource {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = FAVORITES_FILE_NAME,
    )

    override suspend fun addTask(id: Int) {
        context.dataStore.edit { settings ->
            val currentFavorites = settings[FAVORITES_KEY] ?: emptySet()
            settings[FAVORITES_KEY] = currentFavorites + id.toString()
        }
    }

    override suspend fun deleteTask(id: Int) {
        context.dataStore.edit { settings ->
            val currentFavorites = settings[FAVORITES_KEY] ?: emptySet()
            settings[FAVORITES_KEY] = currentFavorites - id.toString()
        }
    }

    override suspend fun getTaskIds(): List<Int> {
        val favorites = context.dataStore.data
            .map { preferences ->
                preferences[FAVORITES_KEY] ?: emptySet()
            }
            .firstOrNull()
        val favoriteIds = favorites?.map { favoriteId ->
            favoriteId.toInt()
        }
        return favoriteIds.orEmpty()
    }

    private companion object {
        private const val FAVORITES_FILE_NAME = "com.yugyd.quiz.favorites"
        private val FAVORITES_KEY = stringSetPreferencesKey(
            "${FAVORITES_FILE_NAME}.favorites_set",
        )
    }
}