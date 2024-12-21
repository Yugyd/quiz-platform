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

package com.yugyd.quiz.ai.connection.api.internal.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.yugyd.quiz.ai.connection.api.AiConnectionLocalSource
import com.yugyd.quiz.ai.connection.api.internal.data.mapper.AiConnectionMapper
import com.yugyd.quiz.ai.connection.api.internal.data.model.AiConnectionsEntity
import com.yugyd.quiz.ai.connection.api.model.AiConnectionModel
import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AiConnectionLocalDataSource @Inject constructor(
    private val aiConnectionMapper: AiConnectionMapper,
    aiConnectionsSerializer: AiConnectionsSerializer,
    @ApplicationContext private val context: Context,
    dispatchersProvider: DispatchersProvider,
) : AiConnectionLocalSource {

    private val Context.dataStore: DataStore<AiConnectionsEntity> by dataStore(
        fileName = AI_CONNECTION_NAME,
        serializer = aiConnectionsSerializer,
        scope = CoroutineScope(dispatchersProvider.io + SupervisorJob()),
    )

    override suspend fun setConnections(connections: List<AiConnectionModel>) {
        context.dataStore.updateData {
            val entities = connections.map(aiConnectionMapper::map)
            it.copy(connections = entities)
        }
    }

    override suspend fun getConnections(): List<AiConnectionModel> {
        return subscribeToConnections().firstOrNull().orEmpty()
    }

    override fun subscribeToConnections(): Flow<List<AiConnectionModel>> {
        return context.dataStore.data.map { connectionModel ->
            val connections = connectionModel.connections
            connections.map(aiConnectionMapper::map)
        }
    }

    private companion object {
        private const val AI_CONNECTION_NAME = "com.yugyd.ai.connection"
    }
}
