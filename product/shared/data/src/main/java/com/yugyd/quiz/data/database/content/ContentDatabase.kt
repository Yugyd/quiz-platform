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

package com.yugyd.quiz.data.database.content

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yugyd.quiz.data.database.content.converters.QuestTypeEntityConverter
import com.yugyd.quiz.data.database.content.dao.ContentResetDao
import com.yugyd.quiz.data.database.content.dao.QuestDao
import com.yugyd.quiz.data.database.content.dao.ThemeDao
import com.yugyd.quiz.data.model.ThemeEntity
import com.yugyd.quiz.data.model.quest.QuestEntity

private const val CONTENT_DB_VERSION = 7

@Database(
    entities = [
        ThemeEntity::class,
        QuestEntity::class
    ],
    version = CONTENT_DB_VERSION,
    exportSchema = true,
)
@TypeConverters(QuestTypeEntityConverter::class)
internal abstract class ContentDatabase : RoomDatabase() {
    abstract fun themeDao(): ThemeDao
    abstract fun questDao(): QuestDao
    abstract fun resetDao(): ContentResetDao
}
