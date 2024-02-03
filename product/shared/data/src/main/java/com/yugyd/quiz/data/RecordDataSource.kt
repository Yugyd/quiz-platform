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

package com.yugyd.quiz.data

import com.yugyd.quiz.data.database.user.dao.RecordDao
import com.yugyd.quiz.data.database.user.dao.UserResetDao
import com.yugyd.quiz.data.model.mappers.RecordEntityMapper
import com.yugyd.quiz.domain.api.model.Mode
import com.yugyd.quiz.domain.api.model.Record
import com.yugyd.quiz.domain.api.repository.RecordSource
import javax.inject.Inject

internal class RecordDataSource @Inject constructor(
    private val recordDao: RecordDao,
    private val resetDao: UserResetDao,
    private val recordEntityMapper: RecordEntityMapper,
) : RecordSource {

    override suspend fun addRecord(record: Record) = recordDao
        .insertRecord(recordEntityMapper.mapToEntity(record))

    override suspend fun updateRecord(record: Record) = recordDao
        .updateRecord(recordEntityMapper.mapToEntity(record))

    override suspend fun deleteRecord(themeId: Int) = recordDao
        .deleteRecordByTheme(themeId)

    override suspend fun deleteRecord(themeId: Int, modeId: Int) = recordDao
        .deleteRecordByThemeAndModeIds(themeId, modeId)

    override suspend fun getRecordsByTheme(theme: Int) = recordDao
        .getRecordsByTheme(theme)
        .map(recordEntityMapper::mapToDomain)

    override suspend fun getRecordsByMode(mode: Mode) = recordDao
        .getRecordsByMode(mode.id)
        .map(recordEntityMapper::mapToDomain)

    override suspend fun getRecords() = recordDao
        .getRecords()
        .map(recordEntityMapper::mapToDomain)

    override suspend fun getRecord(themeId: Int, mode: Mode) = recordDao
        .getRecordByThemeAndModeIds(themeId, mode.id)
        ?.let(recordEntityMapper::mapToDomain)

    override suspend fun getRecordValue(themeId: Int, modeId: Int) = recordDao
        .getRecordValueByThemeAndModeIds(themeId, modeId)

    override suspend fun resetThemes() = resetDao
        .resetRecord()
}
