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

package com.yugyd.quiz.domain.model.data

data class Quest(
    val id: Int = 0,
    val quest: String = "",
    val trueAnswer: String = "",
    val answer2: String = "",
    val answer3: String = "",
    val answer4: String = "",
    val answer5: String = "",
    val answer6: String = "",
    val answer7: String = "",
    val answer8: String = "",
    val complexity: Int = 0,
    val category: Int = 0,
    val section: Int = 0
)
