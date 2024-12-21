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

package com.yugyd.quiz.domain.aiconnection.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.yugyd.quiz.domain.aiconnection.AiSettingsLocalSource
import com.yugyd.quiz.domain.aiconnection.model.AiTermCache
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiSettingsLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
) : AiSettingsLocalSource {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = SEARCH_COURSE_FILE_NAME,
    )

    override fun subscribeToAiTermCache(): Flow<AiTermCache> {
        return context.dataStore.data.map { preferences ->
            preferences[AI_TERM_CACHE_KEY]?.let {
                AiTermCache.fromValue(it)
            } ?: AiTermCache.NONE
        }
    }

    override suspend fun addAiTermCache(model: AiTermCache) {
        context.dataStore.edit { settings ->
            settings[AI_TERM_CACHE_KEY] = model.daysCount
        }
    }

    override fun subscribeToDirectConnection(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[IS_DIRECT_CONNECTION_KEY] ?: false
        }
    }

    override suspend fun setDirectConnection(isDirectConnection: Boolean) {
        context.dataStore.edit { settings ->
            settings[IS_DIRECT_CONNECTION_KEY] = isDirectConnection
        }
    }

    override fun subscribeToAiEnabled(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[IS_AI_ENABLED_KEY] ?: false
        }
    }

    override suspend fun setAiEnabled(isAiEnabled: Boolean) {
        context.dataStore.edit { settings ->
            settings[IS_AI_ENABLED_KEY] = isAiEnabled
        }
    }

    private companion object {
        private const val SEARCH_COURSE_FILE_NAME = "com.yugyd.quiz.aisettings"

        private val AI_TERM_CACHE_KEY = intPreferencesKey("ai_term_cache")
        private val IS_DIRECT_CONNECTION_KEY = booleanPreferencesKey("is_direct_connection")
        private val IS_AI_ENABLED_KEY = booleanPreferencesKey("is_ai_enabled")
    }
}