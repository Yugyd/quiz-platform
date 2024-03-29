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

import com.yugyd.quiz.data.database.user.dao.TrainDao
import com.yugyd.quiz.data.model.mappers.TrainEntityMapper
import com.yugyd.quiz.domain.api.repository.TrainSource
import javax.inject.Inject

internal class TrainDataSource @Inject constructor(
    private val trainDao: TrainDao,
    private val trainEntityMapper: TrainEntityMapper
) : TrainSource {

    override suspend fun getAll() = trainDao
        .getAll()
        .let(trainEntityMapper::mapToDomain)

    override suspend fun addAll(ids: List<Int>) = trainDao
        .insertAll(trainEntityMapper.mapToEntity(ids))

    override suspend fun getTotalProgress(ids: IntArray) = trainDao
        .loadAllByIds(ids)
        .size

    override suspend fun deleteAll(ids: List<Int>) = trainDao
        .deleteAll(trainEntityMapper.mapToEntity(ids))
}
