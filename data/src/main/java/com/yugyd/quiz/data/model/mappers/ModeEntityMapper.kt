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

import com.yugyd.quiz.domain.api.model.Mode
import javax.inject.Inject

class ModeEntityMapper @Inject constructor() {

    fun mapToDomain(id: Int) = when (id) {
        Mode.ARCADE.id -> Mode.ARCADE
        Mode.MARATHON.id -> Mode.MARATHON
        Mode.TRAIN.id -> Mode.TRAIN
        Mode.ERROR.id -> Mode.ERROR
        Mode.NONE.id -> Mode.NONE
        else -> throw IllegalArgumentException()
    }
}