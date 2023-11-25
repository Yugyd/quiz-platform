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

package com.yugyd.quiz.data.model.mappers

import com.yugyd.quiz.data.model.RecordEntity
import com.yugyd.quiz.domain.api.model.Record
import javax.inject.Inject

class RecordEntityMapper @Inject constructor(private val modeEntityMapper: ModeEntityMapper) {

    fun mapToEntity(model: Record) = model.run {
        RecordEntity(
            id = id,
            themeId = themeId,
            modeId = mode.id,
            record = record,
            complexity = null,
            totalTime = totalTime
        )
    }

    fun mapToDomain(entity: RecordEntity) = entity.run {
        Record(
            id = id,
            themeId = themeId,
            mode = modeEntityMapper.mapToDomain(modeId),
            record = record,
            totalTime = totalTime
        )
    }
}
