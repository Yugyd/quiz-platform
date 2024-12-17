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
import org.junit.Rule
import org.junit.Test
import java.io.IOException

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
        migrationTestHelper.createDatabase(
            name = TEST_DB,
            version = 5,
        ).apply {
            execSQL(
                """
                    INSERT INTO quest (_id, quest, true_answer, answer2, answer3, answer4, answer5, 
                    answer6, answer7, answer8, complexity, category, section) VALUES 
                    (1, 'Quest', 
                    'True', 'Answer2', 
                    'Answer3', 'Answer4', 
                    'Answer5', 'Answer6', 
                    'Answer7', 'Answer8', 
                    2, 3, 
                    4)
                """
                    .trimIndent()
            )

            close()
        }

        // WHEN
        migrationTestHelper.runMigrationsAndValidate(
            name = TEST_DB,
            version = 6,
            validateDroppedTables = true,
            MIGRATION_5_6,
        )
    }

    @Test
    @Throws(IOException::class)
    fun migrate6To7() {
        // GIVEN
        migrationTestHelper.createDatabase(
            name = TEST_DB,
            version = 6,
        ).apply {
            execSQL(
                """
                    INSERT INTO quest (_id, quest, true_answer, answer2, answer3, answer4, answer5, 
                    answer6, answer7, answer8, complexity, category, section, type) VALUES 
                    (1, 'Quest', 
                    'True', 'Answer2', 
                    'Answer3', 'Answer4', 
                    'Answer5', 'Answer6', 
                    'Answer7', 'Answer8', 
                    2, 3, 
                    4, 'simple')
                """
                    .trimIndent()
            )

            close()
        }

        // WHEN
        migrationTestHelper.runMigrationsAndValidate(
            name = TEST_DB,
            version = 7,
            validateDroppedTables = true,
            MIGRATION_6_7,
        )
    }
}
