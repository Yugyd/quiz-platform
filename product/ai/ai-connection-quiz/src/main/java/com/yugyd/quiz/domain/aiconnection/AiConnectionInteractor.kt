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

package com.yugyd.quiz.domain.aiconnection

import com.yugyd.quiz.ai.connection.api.model.AiConnectionModel
import com.yugyd.quiz.ai.connection.api.model.AiConnectionProviderModel
import com.yugyd.quiz.ai.connection.api.model.NewAiConnectionModel
import com.yugyd.quiz.ai.connection.api.model.UpdateAiConnectionModel
import com.yugyd.quiz.domain.aiconnection.model.AiInstructionConfig
import com.yugyd.quiz.domain.aiconnection.model.AiTermCache
import kotlinx.coroutines.flow.Flow

interface AiConnectionInteractor {
    suspend fun isActiveAiConnection(): Boolean

    suspend fun subscribeCurrentAiConnection(): Flow<AiConnectionModel?>

    suspend fun getCurrentAiConnection(): AiConnectionModel?

    suspend fun getAiConnection(aiConnectionId: String): AiConnectionModel?

    suspend fun activeAiConnection(id: String): Boolean

    suspend fun addAiConnection(model: NewAiConnectionModel): Boolean

    suspend fun updateAiConnection(model: UpdateAiConnectionModel): Boolean

    suspend fun deleteAiConnection(id: String): Boolean

    suspend fun getAllAiConnections(): List<AiConnectionModel>

    suspend fun getAvailableAiProviders(): List<AiConnectionProviderModel>

    fun subscribeAiTermCache(): Flow<AiTermCache>

    suspend fun addAiTermCache(model: AiTermCache)

    suspend fun getAiTermCaches(): List<AiTermCache>

    suspend fun getAiInstructionConfigs(): List<AiInstructionConfig>

    fun subscribeToDirectConnection(): Flow<Boolean>

    suspend fun setDirectConnection(isDirectConnection: Boolean)

    fun subscribeToAiEnabled(): Flow<Boolean>

    suspend fun setAiEnabled(isAiEnabled: Boolean)

    suspend fun getAiPrivacyUrl(): String?
}
