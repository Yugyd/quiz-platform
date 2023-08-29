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

package com.yugyd.quiz.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yugyd.quiz.data.database.dao.ErrorDao
import com.yugyd.quiz.data.database.dao.RecordDao
import com.yugyd.quiz.data.database.dao.ResetDao
import com.yugyd.quiz.data.database.dao.SectionDao
import com.yugyd.quiz.data.database.dao.TrainDao
import com.yugyd.quiz.data.model.ErrorEntity
import com.yugyd.quiz.data.model.ModeEntity
import com.yugyd.quiz.data.model.RecordEntity
import com.yugyd.quiz.data.model.SectionEntity
import com.yugyd.quiz.data.model.TrainEntity

private const val USER_DB_VERSION = 1

@Database(
    entities = [
        ErrorEntity::class,
        ModeEntity::class,
        RecordEntity::class,
        SectionEntity::class,
        TrainEntity::class
    ],
    version = USER_DB_VERSION,
    exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {
    abstract fun errorDao(): ErrorDao
    abstract fun recordDao(): RecordDao
    abstract fun resetDao(): ResetDao
    abstract fun sectionDao(): SectionDao
    abstract fun trainDao(): TrainDao
}