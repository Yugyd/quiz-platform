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
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yugyd.quiz.data.model.ErrorEntity

@Dao
interface ErrorDao {

    @Query("SELECT _id FROM error")
    suspend fun getIds(): List<Int>

    @Query("SELECT COUNT(_id) FROM error")
    suspend fun isHaveErrors(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(errors: List<ErrorEntity>)

    @Delete
    suspend fun deleteAll(errors: List<ErrorEntity>): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ErrorEntity)

    @Delete
    suspend fun delete(entity: ErrorEntity): Int
}
