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

package com.yugyd.quiz.data.model.quest

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quest")
data class QuestEntity(
    @PrimaryKey @ColumnInfo(name = "_id") val id: Int,
    @ColumnInfo(name = "quest") val quest: String,
    @ColumnInfo(name = "true_answer") val trueAnswer: String,
    @ColumnInfo(name = "answer2") val answer2: String?,
    @ColumnInfo(name = "answer3") val answer3: String?,
    @ColumnInfo(name = "answer4") val answer4: String?,
    @ColumnInfo(name = "answer5") val answer5: String?,
    @ColumnInfo(name = "answer6") val answer6: String?,
    @ColumnInfo(name = "answer7") val answer7: String?,
    @ColumnInfo(name = "answer8") val answer8: String?,
    @ColumnInfo(name = "complexity") val complexity: Int,
    @ColumnInfo(name = "category") val category: Int,
    @ColumnInfo(name = "section") val section: Int,
    @ColumnInfo(
        name = "type",
        defaultValue = QuestTypeEntityConstants.SIMPLE_TYPE_DATABASE_VALUE,
    ) val type: String,
)
