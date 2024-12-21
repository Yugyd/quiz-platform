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

package com.yugyd.quiz.ai.connection.api.di

import com.yugyd.quiz.ai.connection.api.AiConnectionClient
import com.yugyd.quiz.ai.connection.api.AiConnectionClientImpl
import com.yugyd.quiz.ai.connection.api.AiConnectionLocalSource
import com.yugyd.quiz.ai.connection.api.internal.data.AiConnectionLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AiConnectionBlModule {

    @Binds
    @Singleton
    internal abstract fun bindAiConnectionLocalSource(
        impl: AiConnectionLocalDataSource,
    ): AiConnectionLocalSource

    @Binds
    @Singleton
    internal abstract fun bindAiConnectionClient(impl: AiConnectionClientImpl): AiConnectionClient
}
