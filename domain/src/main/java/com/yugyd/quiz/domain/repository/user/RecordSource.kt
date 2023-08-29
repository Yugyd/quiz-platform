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

package com.yugyd.quiz.domain.repository.user

import com.yugyd.quiz.domain.model.data.Record
import com.yugyd.quiz.domain.model.share.Mode

interface RecordSource {
    suspend fun addRecord(record: Record)
    suspend fun updateRecord(record: Record)
    suspend fun deleteRecord(themeId: Int): Int
    suspend fun deleteRecord(themeId: Int, modeId: Int): Int
    suspend fun getRecordsByTheme(theme: Int): List<Record>
    suspend fun getRecordsByMode(mode: Mode): List<Record>
    suspend fun getRecords(): List<Record>
    suspend fun getRecord(themeId: Int, mode: Mode): Record?
    suspend fun getRecordValue(themeId: Int, modeId: Int): Int
    suspend fun resetThemes(): Int
}
