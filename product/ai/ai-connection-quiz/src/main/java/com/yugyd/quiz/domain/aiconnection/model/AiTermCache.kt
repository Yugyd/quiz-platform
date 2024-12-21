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

package com.yugyd.quiz.domain.aiconnection.model

enum class AiTermCache(val daysCount: Int) {
    NONE(daysCount = 0),
    ONE_DAY(daysCount = 1),
    TWO_DAY(daysCount = 2),
    ONE_WEEK(daysCount = 7),
    ONE_MONTH(daysCount = 31);

    companion object {
        fun fromValue(value: Int): AiTermCache {
            return entries.firstOrNull { it.daysCount == value } ?: NONE
        }
    }
}
