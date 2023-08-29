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

package com.yugyd.quiz.domain.model.share

enum class Mode(val id: Int) {
    ARCADE(id = 4),
    MARATHON(id = 0),
    TRAIN(id = 2),
    ERROR(id = 3),
    NONE(id = -1);

    companion object {
        fun fromId(id: Int): Mode {
            return when (id) {
                ARCADE.id -> ARCADE
                MARATHON.id -> MARATHON
                TRAIN.id -> TRAIN
                ERROR.id -> ERROR
                else -> NONE
            }
        }
    }
}
