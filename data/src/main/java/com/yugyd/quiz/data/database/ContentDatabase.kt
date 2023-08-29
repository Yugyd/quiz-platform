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
import com.yugyd.quiz.data.database.dao.QuestDao
import com.yugyd.quiz.data.database.dao.ThemeDao
import com.yugyd.quiz.data.model.QuestEntity
import com.yugyd.quiz.data.model.ThemeEntity

@Database(
    entities = [
        ThemeEntity::class,
        QuestEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class ContentDatabase : RoomDatabase() {
    abstract fun themeDao(): ThemeDao
    abstract fun questDao(): QuestDao
}
