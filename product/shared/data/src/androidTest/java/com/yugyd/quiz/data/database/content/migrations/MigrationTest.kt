package com.yugyd.quiz.data.database.content.migrations

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

import androidx.room.testing.MigrationTestHelper
import androidx.test.platform.app.InstrumentationRegistry
import com.yugyd.quiz.data.database.content.ContentDatabase
import com.yugyd.quiz.data.model.quest.QuestEntity
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MigrationTest {

    private val TEST_DB = "migration-androidTest"

    @get:Rule
    val migrationTestHelper: MigrationTestHelper = MigrationTestHelper(
        instrumentation = InstrumentationRegistry.getInstrumentation(),
        databaseClass = ContentDatabase::class.java,
    )

    @Test
    @Throws(IOException::class)
    fun migrate5To6() {
        // GIVEN
        val testQuestEntity = QuestEntity(
            id = 1,
            quest = "Quest",
            trueAnswer = "True answer",
            answer2 = "Answer2",
            answer3 = "Answer3",
            answer4 = "Answer4",
            answer5 = "Answer5",
            answer6 = "Answer6",
            answer7 = "Answer7",
            answer8 = "Answer8",
            complexity = 2,
            category = 3,
            section = 4,
            type = "simple",
        )
        // Create the database in version 5
        migrationTestHelper.createDatabase(
            name = TEST_DB,
            version = 5,
        ).apply {
            execSQL(
                """
                    INSERT INTO quest (_id, quest, true_answer, answer2, answer3, answer4, answer5, 
                    answer6, answer7, answer8, complexity, category, section) VALUES 
                    (${testQuestEntity.id}, '${testQuestEntity.quest}', 
                    '${testQuestEntity.trueAnswer}', '${testQuestEntity.answer2}', 
                    '${testQuestEntity.answer3}', '${testQuestEntity.answer4}', 
                    '${testQuestEntity.answer5}', '${testQuestEntity.answer6}', 
                    '${testQuestEntity.answer7}', '${testQuestEntity.answer8}', 
                    ${testQuestEntity.complexity}, ${testQuestEntity.category}, 
                    ${testQuestEntity.section})
                """
                    .trimIndent()
            )

            close()
        }

        // WHEN
        // Re-open the database with version 6 and apply the migration
        val db = migrationTestHelper.runMigrationsAndValidate(
            name = TEST_DB,
            version = 6,
            validateDroppedTables = true,
            MIGRATION_5_6,
        )

        // THEN
        // Validate that the migration was successful
        db.query("SELECT * FROM quest").use { cursor ->
            assertTrue(cursor.moveToFirst())

            val actualQuestEntity = QuestEntity(
                id = cursor.getInt(cursor.getColumnIndex("_id")),
                quest = cursor.getString(cursor.getColumnIndex("quest")),
                trueAnswer = cursor.getString(cursor.getColumnIndex("true_answer")),
                answer2 = cursor.getString(cursor.getColumnIndex("answer2")),
                answer3 = cursor.getString(cursor.getColumnIndex("answer3")),
                answer4 = cursor.getString(cursor.getColumnIndex("answer4")),
                answer5 = cursor.getString(cursor.getColumnIndex("answer5")),
                answer6 = cursor.getString(cursor.getColumnIndex("answer6")),
                answer7 = cursor.getString(cursor.getColumnIndex("answer7")),
                answer8 = cursor.getString(cursor.getColumnIndex("answer8")),
                complexity = cursor.getInt(cursor.getColumnIndex("complexity")),
                category = cursor.getInt(cursor.getColumnIndex("category")),
                section = cursor.getInt(cursor.getColumnIndex("section")),
                type = cursor.getString(cursor.getColumnIndex("type")),
            )
            assertEquals(
                expected = testQuestEntity,
                actual = actualQuestEntity,
            )
        }
    }
}
