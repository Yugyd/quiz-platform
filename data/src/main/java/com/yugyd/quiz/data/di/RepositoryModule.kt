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

import android.content.Context
import androidx.room.Room
import com.yugyd.quiz.data.ErrorDataSource
import com.yugyd.quiz.data.LoggerImpl
import com.yugyd.quiz.data.PreferencesDataSource
import com.yugyd.quiz.data.QuestDataSource
import com.yugyd.quiz.data.RecordDataSource
import com.yugyd.quiz.data.SectionDataSource
import com.yugyd.quiz.data.ThemeDataSource
import com.yugyd.quiz.data.TrainDataSource
import com.yugyd.quiz.data.database.ContentDatabase
import com.yugyd.quiz.data.database.UserDatabase
import com.yugyd.quiz.data.model.mappers.ErrorEntityMapper
import com.yugyd.quiz.data.model.mappers.QuestEntityMapper
import com.yugyd.quiz.data.model.mappers.RecordEntityMapper
import com.yugyd.quiz.data.model.mappers.SectionEntityMapper
import com.yugyd.quiz.data.model.mappers.ThemeEntityMapper
import com.yugyd.quiz.data.model.mappers.TrainEntityMapper
import com.yugyd.quiz.domain.repository.Logger
import com.yugyd.quiz.domain.repository.PreferencesSource
import com.yugyd.quiz.domain.repository.content.QuestSource
import com.yugyd.quiz.domain.repository.content.ThemeSource
import com.yugyd.quiz.domain.repository.user.ErrorSource
import com.yugyd.quiz.domain.repository.user.RecordSource
import com.yugyd.quiz.domain.repository.user.SectionSource
import com.yugyd.quiz.domain.repository.user.TrainSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun providePreferencesSource(
        @ApplicationContext context: Context
    ): PreferencesSource = PreferencesDataSource(context)

    @Singleton
    @Provides
    fun provideQuestSource(
        db: ContentDatabase,
        questEntityMapper: QuestEntityMapper
    ): QuestSource = QuestDataSource(db.questDao(), questEntityMapper)

    @Singleton
    @Provides
    fun provideThemeSource(
        db: ContentDatabase,
        themeEntityMapper: ThemeEntityMapper
    ): ThemeSource = ThemeDataSource(db.questDao(), db.themeDao(), themeEntityMapper)

    @Singleton
    @Provides
    fun provideContentDatabase(
        @ApplicationContext appContext: Context
    ) = Room
        .databaseBuilder(
            appContext,
            ContentDatabase::class.java,
            CONTENT_DB_NAME
        )
        .createFromAsset(CONTENT_DB_NAME)
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideErrorSource(
        db: UserDatabase,
        errorEntityMapper: ErrorEntityMapper
    ): ErrorSource = ErrorDataSource(db.errorDao(), errorEntityMapper)

    @Singleton
    @Provides
    fun provideRecordSource(
        db: UserDatabase,
        recordEntityMapper: RecordEntityMapper,
    ): RecordSource = RecordDataSource(
        db.recordDao(),
        db.resetDao(),
        recordEntityMapper
    )

    @Singleton
    @Provides
    fun provideSectionSource(
        contentDb: ContentDatabase,
        userDb: UserDatabase,
        sectionEntityMapper: SectionEntityMapper
    ): SectionSource = SectionDataSource(
        contentDb.questDao(),
        userDb.sectionDao(),
        userDb.resetDao(),
        sectionEntityMapper
    )

    @Singleton
    @Provides
    fun provideTrainSource(
        db: UserDatabase,
        trainEntityMapper: TrainEntityMapper
    ): TrainSource = TrainDataSource(db.trainDao(), trainEntityMapper)

    @Singleton
    @Provides
    fun provideUserDatabase(
        @ApplicationContext appContext: Context
    ) = Room
        .databaseBuilder(appContext, UserDatabase::class.java, USER_DB_NAME)
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideLogger(
        @ApplicationContext appContext: Context
    ): Logger = LoggerImpl(appContext)

    companion object {
        private const val USER_DB_NAME = "user-data.db"
        private const val CONTENT_DB_NAME = "content-encode-pro.db"
    }
}