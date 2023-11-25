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
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class ContentResetDao {

    @Query("DELETE FROM category")
    abstract suspend fun resetCategory(): Int

    @Query("DELETE FROM quest")
    abstract suspend fun resetQuest(): Int

    @Transaction
    open suspend fun reset() {
        resetCategory()
        resetQuest()
    }
}
