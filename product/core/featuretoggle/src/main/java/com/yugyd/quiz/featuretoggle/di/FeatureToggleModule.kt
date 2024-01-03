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

package com.yugyd.quiz.featuretoggle.di

import com.yugyd.quiz.remoteconfig.api.RemoteConfig
import com.yugyd.quiz.featuretoggle.data.RemoteConfigImpl
import com.yugyd.quiz.featuretoggle.data.RemoteConfigRepositoryImpl
import com.yugyd.quiz.featuretoggle.domain.FeatureManager
import com.yugyd.quiz.featuretoggle.domain.FeatureManagerImpl
import com.yugyd.quiz.featuretoggle.domain.RemoteConfigRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface FeatureToggleModule {

    @Binds
    fun bindRemoteConfig(impl: RemoteConfigImpl): RemoteConfig

    @Binds
    fun bindRemoteConfigRepository(impl: RemoteConfigRepositoryImpl): RemoteConfigRepository

    @Binds
    fun bindFeatureManager(impl: FeatureManagerImpl): FeatureManager
}
