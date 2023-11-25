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

package com.yugyd.quiz.data.database.user.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.yugyd.quiz.data.model.ContentEntity

@Dao
interface ContentDao {

    @Query("SELECT * FROM content")
    suspend fun getAll(): List<ContentEntity>

    @Query("SELECT * FROM content WHERE is_checked = ${true}")
    suspend fun getSelectedContent(): ContentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ContentEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(entity: ContentEntity)

    @Query("DELETE FROM content WHERE _id = :contentId")
    suspend fun delete(contentId: Int): Int
}
