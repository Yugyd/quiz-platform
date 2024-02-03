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

package com.yugyd.quiz.data.di

import com.yugyd.quiz.core.Logger
import com.yugyd.quiz.core.file.FileRepository
import com.yugyd.quiz.core.file.FileRepositoryImpl
import com.yugyd.quiz.data.ContentResetDataSource
import com.yugyd.quiz.data.ErrorDataSource
import com.yugyd.quiz.data.LoggerImpl
import com.yugyd.quiz.data.PreferencesDataSource
import com.yugyd.quiz.data.QuestDataSource
import com.yugyd.quiz.data.RecordDataSource
import com.yugyd.quiz.data.SectionDataSource
import com.yugyd.quiz.data.ThemeDataSource
import com.yugyd.quiz.data.TrainDataSource
import com.yugyd.quiz.data.UserResetDataSource
import com.yugyd.quiz.domain.api.repository.ContentResetSource
import com.yugyd.quiz.domain.api.repository.ErrorSource
import com.yugyd.quiz.domain.api.repository.PreferencesSource
import com.yugyd.quiz.domain.api.repository.QuestSource
import com.yugyd.quiz.domain.api.repository.RecordSource
import com.yugyd.quiz.domain.api.repository.SectionSource
import com.yugyd.quiz.domain.api.repository.ThemeSource
import com.yugyd.quiz.domain.api.repository.TrainSource
import com.yugyd.quiz.domain.api.repository.UserResetSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    internal abstract fun bindPreferencesSource(
        impl: PreferencesDataSource
    ): PreferencesSource

    @Binds
    internal abstract fun bindQuestSource(
        impl: QuestDataSource,
    ): QuestSource

    @Binds
    internal abstract fun bindThemeSource(
        impl: ThemeDataSource,
    ): ThemeSource

    @Binds
    internal abstract fun bindContentResetSource(
        impl: ContentResetDataSource,
    ): ContentResetSource

    @Binds
    internal abstract fun bindErrorSource(
        impl: ErrorDataSource,
    ): ErrorSource

    @Binds
    internal abstract fun bindRecordSource(
        impl: RecordDataSource,
    ): RecordSource

    @Binds
    internal abstract fun bindSectionSource(
        impl: SectionDataSource,
    ): SectionSource

    @Binds
    internal abstract fun bindTrainSource(
        impl: TrainDataSource,
    ): TrainSource

    @Binds
    internal abstract fun bindUserResetSource(
        impl: UserResetDataSource,
    ): UserResetSource

    @Singleton
    @Binds
    internal abstract fun bindLogger(
        impl: LoggerImpl,
    ): Logger

    @Binds
    internal abstract fun bindFileRepository(
        impl: FileRepositoryImpl,
    ): FileRepository
}