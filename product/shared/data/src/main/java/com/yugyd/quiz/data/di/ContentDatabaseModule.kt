package com.yugyd.quiz.data.di

import android.content.Context
import androidx.room.Room
import com.yugyd.quiz.core.GlobalConfig
import com.yugyd.quiz.data.database.content.ContentDatabase
import com.yugyd.quiz.data.database.content.dao.ContentResetDao
import com.yugyd.quiz.data.database.content.dao.QuestDao
import com.yugyd.quiz.data.database.content.dao.ThemeDao
import com.yugyd.quiz.data.database.content.migrations.MIGRATION_5_6
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ContentDatabaseModule {

    private const val CONTENT_DB_NAME = "content-encode-pro.db"

    @Singleton
    @Provides
    internal fun provideContentDatabase(
        @ApplicationContext appContext: Context
    ) = Room
        .databaseBuilder(
            appContext,
            ContentDatabase::class.java,
            CONTENT_DB_NAME,
        )
        .addMigrations(
            MIGRATION_5_6,
        )
        .let {
            if (GlobalConfig.IS_BASED_ON_PLATFORM_APP) {
                it.createFromAsset(CONTENT_DB_NAME)
            } else {
                it
            }
        }
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    internal fun provideThemeDao(db: ContentDatabase): ThemeDao = db.themeDao()

    @Provides
    internal fun provideQuestDao(db: ContentDatabase): QuestDao = db.questDao()

    @Provides
    internal fun provideContentResetDao(db: ContentDatabase): ContentResetDao = db.resetDao()
}