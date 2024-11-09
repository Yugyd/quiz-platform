package com.yugyd.quiz.data.database.user

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yugyd.quiz.data.database.user.dao.ContentDao
import com.yugyd.quiz.data.database.user.dao.ErrorDao
import com.yugyd.quiz.data.database.user.dao.RecordDao
import com.yugyd.quiz.data.database.user.dao.SectionDao
import com.yugyd.quiz.data.database.user.dao.TrainDao
import com.yugyd.quiz.data.database.user.dao.UserResetDao
import com.yugyd.quiz.data.model.ContentEntity
import com.yugyd.quiz.data.model.ErrorEntity
import com.yugyd.quiz.data.model.ModeEntity
import com.yugyd.quiz.data.model.RecordEntity
import com.yugyd.quiz.data.model.SectionEntity
import com.yugyd.quiz.data.model.TrainEntity

private const val USER_DB_VERSION = 2

@Database(
    entities = [
        ErrorEntity::class,
        ModeEntity::class,
        RecordEntity::class,
        SectionEntity::class,
        TrainEntity::class,
        ContentEntity::class,
    ],
    version = USER_DB_VERSION,
    exportSchema = true
)
internal abstract class UserDatabase : RoomDatabase() {
    abstract fun errorDao(): ErrorDao
    abstract fun recordDao(): RecordDao
    abstract fun resetDao(): UserResetDao
    abstract fun sectionDao(): SectionDao
    abstract fun trainDao(): TrainDao
    abstract fun contentDao(): ContentDao
}