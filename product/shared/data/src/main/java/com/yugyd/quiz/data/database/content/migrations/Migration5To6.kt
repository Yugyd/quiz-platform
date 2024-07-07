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

package com.yugyd.quiz.data.database.content.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Create a new temporary table with the updated schema
        db.execSQL(
            """
            CREATE TABLE quest_new (
                _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                quest TEXT NOT NULL,
                true_answer TEXT NOT NULL,
                answer2 TEXT,
                answer3 TEXT,
                answer4 TEXT,
                answer5 TEXT,
                answer6 TEXT,
                answer7 TEXT,
                answer8 TEXT,
                complexity INTEGER NOT NULL,
                category INTEGER NOT NULL,
                section INTEGER NOT NULL,
                type TEXT NOT NULL DEFAULT 'simple'
            )
        """.trimIndent()
        )

        // Copy the data from the old table to the new table
        db.execSQL(
            """
            INSERT INTO quest_new (_id, quest, true_answer, answer2, answer3, answer4, answer5, answer6, answer7, answer8, complexity, category, section, type)
            SELECT _id, quest, true_answer, answer2, answer3, answer4, answer5, answer6, answer7, answer8, complexity, category, section, 'simple'
            FROM quest
        """.trimIndent()
        )

        // Remove the old table
        db.execSQL("DROP TABLE quest")

        // Rename the new table to the old table name
        db.execSQL("ALTER TABLE quest_new RENAME TO quest")
    }
}
