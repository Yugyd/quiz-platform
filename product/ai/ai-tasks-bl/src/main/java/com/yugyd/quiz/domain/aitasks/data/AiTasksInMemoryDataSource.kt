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

package com.yugyd.quiz.domain.aitasks.data

import com.yugyd.quiz.domain.aitasks.AiTasksInMemorySource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AiTasksInMemoryDataSource @Inject constructor() : AiTasksInMemorySource {

    private val mutableStateFlow = MutableStateFlow(AiTasksModel())

    override fun subscribeToCachedAiTasks(): Flow<AiTasksModel> {
        return mutableStateFlow.asStateFlow()
    }

    override suspend fun updateCachedAiTasks(aiTasks: AiTasksModel) {
        mutableStateFlow.update { aiTasks }
    }
}
