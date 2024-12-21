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

package com.yugyd.quiz.ai.connection.api

import com.yugyd.quiz.ai.connection.api.model.AiConnectionModel
import com.yugyd.quiz.ai.connection.api.model.AiConnectionProviderModel
import com.yugyd.quiz.ai.connection.api.model.NewAiConnectionModel
import com.yugyd.quiz.ai.connection.api.model.UpdateAiConnectionModel
import kotlinx.coroutines.flow.Flow

interface AiConnectionClient {
    suspend fun isActiveAiConnection(): Boolean

    fun subscribeToCurrentAiConnection(): Flow<AiConnectionModel?>

    suspend fun addAiConnection(model: NewAiConnectionModel): Boolean

    suspend fun updateAiConnection(model: UpdateAiConnectionModel): Boolean

    suspend fun deleteAiConnection(id: String): Boolean

    suspend fun activeAiConnection(id: String): Boolean

    suspend fun getAllAiConnections(): List<AiConnectionModel>

    suspend fun getAvailableAiProviders(): List<AiConnectionProviderModel>
}
