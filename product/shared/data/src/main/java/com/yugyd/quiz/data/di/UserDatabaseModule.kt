package com.yugyd.quiz.data.di

import android.content.Context
import androidx.room.Room
import com.yugyd.quiz.data.database.user.UserDatabase
import com.yugyd.quiz.data.database.user.dao.ContentDao
import com.yugyd.quiz.data.database.user.dao.ErrorDao
import com.yugyd.quiz.data.database.user.dao.RecordDao
import com.yugyd.quiz.data.database.user.dao.SectionDao
import com.yugyd.quiz.data.database.user.dao.TrainDao
import com.yugyd.quiz.data.database.user.dao.UserResetDao
import com.yugyd.quiz.data.database.user.migrations.MIGRATION_1_2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserDatabaseModule {

    private const val USER_DB_NAME = "user-data.db"

    @Singleton
    @Provides
    internal fun provideUserDatabase(
        @ApplicationContext appContext: Context
    ) = Room
        .databaseBuilder(appContext, UserDatabase::class.java, USER_DB_NAME)
        .addMigrations(MIGRATION_1_2)
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    internal fun provideErrorDao(db: UserDatabase): ErrorDao = db.errorDao()

    @Provides
    internal fun provideRecordDao(db: UserDatabase): RecordDao = db.recordDao()

    @Provides
    internal fun provideUserResetDao(db: UserDatabase): UserResetDao = db.resetDao()

    @Provides
    internal fun provideSectionDao(db: UserDatabase): SectionDao = db.sectionDao()

    @Provides
    internal fun provideTrainDao(db: UserDatabase): TrainDao = db.trainDao()

    @Provides
    internal fun provideContentDao(db: UserDatabase): ContentDao = db.contentDao()

}
