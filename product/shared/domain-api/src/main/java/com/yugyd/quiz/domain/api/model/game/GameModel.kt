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

package com.yugyd.quiz.domain.api.model.game

data class GameModel(
    val questIds: List<Int> = listOf(),
    val sectionIds: Set<Int> = setOf(),
    val trainIds: Set<Int> = setOf(),
    val errorIds: Set<Int> = setOf(),
    val gameSessionErrors: Set<Int> = setOf(),
    val condition: Int = 0,
    val point: Int = 0,
    val steep: Int = 0,
    val questCount: Int = 0,
    val isFinished: Boolean = false,
    val startTime: Long = 0,
    val endTime: Long = 0,
    val isShowRewarded: Boolean = false,
    val isHaveRewardedItem: Boolean = false
)
