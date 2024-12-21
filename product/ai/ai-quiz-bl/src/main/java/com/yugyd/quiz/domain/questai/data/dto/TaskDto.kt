/*
 * Copyright 2024 Roman Likhachev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yugyd.quiz.domain.questai.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskDto(
    @SerialName("id") val id: Int,
    @SerialName("quest") val quest: String,
    @SerialName("image") val image: String? = null,
    @SerialName("trueAnswer") val trueAnswer: String,
    @SerialName("answer2") val answer2: String? = null,
    @SerialName("answer3") val answer3: String? = null,
    @SerialName("answer4") val answer4: String? = null,
    @SerialName("answer5") val answer5: String? = null,
    @SerialName("answer6") val answer6: String? = null,
    @SerialName("answer7") val answer7: String? = null,
    @SerialName("answer8") val answer8: String? = null,
    @SerialName("complexity") val complexity: Int,
    @SerialName("category") val category: Int,
    @SerialName("section") val section: Int,
    @SerialName("type") val type: String = "simple",
)
