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

import com.yugyd.quiz.domain.aiconnection.data.AiRemoteConfigDataSource
import com.yugyd.quiz.domain.aiconnection.data.AiSettingsLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AiConnectionBlModule {

    @Binds
    internal abstract fun bindAiRemoteConfigSource(
        impl: AiRemoteConfigDataSource,
    ): AiRemoteConfigSource

    @Binds
    internal abstract fun bindAiSettingsLocalSource(
        impl: AiSettingsLocalDataSource,
    ): AiSettingsLocalSource


    @Binds
    internal abstract fun bindAiConnectionInteractor(
        impl: AiConnectionInteractorImpl,
    ): AiConnectionInteractor
}
