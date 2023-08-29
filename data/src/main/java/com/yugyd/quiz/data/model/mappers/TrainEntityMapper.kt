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

import com.yugyd.quiz.data.model.TrainEntity
import javax.inject.Inject

class TrainEntityMapper @Inject constructor() {

    fun mapToEntity(ids: List<Int>) = ids.map { mapToEntity(it) }

    fun mapToDomain(entities: List<TrainEntity>) = entities.map { it.id }

    private fun mapToEntity(id: Int) = TrainEntity(id)
}