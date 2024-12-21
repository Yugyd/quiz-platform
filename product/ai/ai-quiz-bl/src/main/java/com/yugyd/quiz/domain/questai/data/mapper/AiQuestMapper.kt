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

package com.yugyd.quiz.domain.questai.data.mapper

import com.yugyd.quiz.domain.questai.data.dto.TaskDto
import com.yugyd.quiz.domain.questai.data.dto.ThemeDetailDto
import com.yugyd.quiz.domain.questai.data.dto.ThemeDto
import com.yugyd.quiz.domain.questai.data.dto.VerifyTaskDto
import com.yugyd.quiz.domain.questai.model.AiVerifyTaskModel
import com.yugyd.quiz.domain.questai.model.AiTaskModel
import com.yugyd.quiz.domain.questai.model.AiThemeDetailModel
import com.yugyd.quiz.domain.questai.model.AiThemeModel

interface AiQuestMapper {
    fun map(dto: TaskDto): AiTaskModel
    fun map(dto: VerifyTaskDto): AiVerifyTaskModel
    fun map(dto: ThemeDto): AiThemeModel
    fun map(dto: ThemeDetailDto): AiThemeDetailModel
}