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

package com.yugyd.quiz.domain.content.di

import android.content.Context
import com.yugyd.quiz.data.database.user.dao.ContentDao
import com.yugyd.quiz.data.model.mappers.ContentEntityMapper
import com.yugyd.quiz.domain.content.ContentPreferencesSource
import com.yugyd.quiz.domain.content.ContentSource
import com.yugyd.quiz.domain.content.data.ContentDataSource
import com.yugyd.quiz.domain.content.data.ContentPreferencesSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
class ContentDataModule {

    @Provides
    fun provideContentPreferencesSource(
        @ApplicationContext context: Context,
    ): ContentPreferencesSource = ContentPreferencesSourceImpl(context)

    @Provides
    fun provideContentSource(
        contentDao: ContentDao,
        contentEntityMapper: ContentEntityMapper,
    ): ContentSource = ContentDataSource(
        contentDao = contentDao,
        entityMapper = contentEntityMapper,
    )
}
