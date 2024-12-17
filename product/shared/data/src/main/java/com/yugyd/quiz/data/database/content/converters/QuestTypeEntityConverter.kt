/*
 * Copyright 2024 Roman Likhachev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yugyd.quiz.data.database.content.converters

import androidx.room.TypeConverter
import com.yugyd.quiz.data.model.quest.QuestTypeEntity

class QuestTypeEntityConverter {

    @TypeConverter
    fun fromQuestType(value: QuestTypeEntity): String {
        return value.databaseValue
    }

    @TypeConverter
    fun toQuestType(value: String): QuestTypeEntity {
        return QuestTypeEntity.entries
            .firstOrNull {
                it.databaseValue == value
            } ?: QuestTypeEntity.NONE
    }
}
