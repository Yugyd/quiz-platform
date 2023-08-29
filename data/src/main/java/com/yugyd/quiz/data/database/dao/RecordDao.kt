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

package com.yugyd.quiz.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.yugyd.quiz.data.model.RecordEntity

@Dao
interface RecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: RecordEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRecord(record: RecordEntity)

    @Query("DELETE FROM record WHERE theme_id = :themeId")
    suspend fun deleteRecordByTheme(themeId: Int): Int

    @Query("DELETE FROM record WHERE theme_id = :themeId AND mode_id = :modeId")
    suspend fun deleteRecordByThemeAndModeIds(themeId: Int, modeId: Int): Int

    @Query("SELECT * FROM record")
    suspend fun getRecords(): List<RecordEntity>

    @Query("SELECT * FROM record where mode_id = :modeId")
    suspend fun getRecordsByMode(modeId: Int): List<RecordEntity>

    @Query("SELECT * FROM record where theme_id = :themeId")
    suspend fun getRecordsByTheme(themeId: Int): List<RecordEntity>

    @Query("SELECT * FROM record WHERE theme_id = :themeId AND mode_id = :modeId")
    suspend fun getRecordByThemeAndModeIds(themeId: Int, modeId: Int): RecordEntity?

    @Query("SELECT record FROM record WHERE theme_id = :themeId AND mode_id = :modeId")
    suspend fun getRecordValueByThemeAndModeIds(themeId: Int, modeId: Int): Int
}
