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

import com.yugyd.quiz.data.database.user.dao.ErrorDao
import com.yugyd.quiz.data.model.mappers.ErrorEntityMapper
import com.yugyd.quiz.domain.api.repository.ErrorSource

class ErrorDataSource(
    private val errorDao: ErrorDao,
    private val errorEntityMapper: ErrorEntityMapper
) : ErrorSource {

    override suspend fun getErrors() = errorDao.getIds()

    override suspend fun isHaveErrors() = errorDao.isHaveErrors()

    override suspend fun addErrors(errors: List<Int>) =
        errorDao.insertAll(errorEntityMapper.mapToEntity(errors))

    override suspend fun removeErrors(errors: List<Int>) = errorDao
        .deleteAll(errorEntityMapper.mapToEntity(errors))

    override suspend fun addError(id: Int) = errorDao
        .insert(errorEntityMapper.mapToEntity(id))

    override suspend fun removeError(id: Int) = errorDao
        .delete(errorEntityMapper.mapToEntity(id))
}
