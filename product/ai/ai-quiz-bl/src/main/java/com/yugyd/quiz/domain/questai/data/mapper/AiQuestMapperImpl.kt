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
import com.yugyd.quiz.domain.questai.model.AiTaskModel
import com.yugyd.quiz.domain.questai.model.AiThemeDetailModel
import com.yugyd.quiz.domain.questai.model.AiThemeModel
import com.yugyd.quiz.domain.questai.model.AiVerifyTaskModel
import javax.inject.Inject

class AiQuestMapperImpl @Inject internal constructor() : AiQuestMapper {

    override fun map(dto: TaskDto): AiTaskModel {
        return AiTaskModel(
            id = dto.id,
            quest = dto.quest,
            image = dto.image,
            trueAnswer = dto.trueAnswer,
            answer2 = dto.answer2,
            answer3 = dto.answer3,
            answer4 = dto.answer4,
            answer5 = dto.answer5,
            answer6 = dto.answer6,
            answer7 = dto.answer7,
            answer8 = dto.answer8,
            complexity = dto.complexity,
            category = dto.category,
            section = dto.section,
            type = dto.type,
        )
    }

    override fun map(dto: VerifyTaskDto): AiVerifyTaskModel {
        return AiVerifyTaskModel(
            aiTrueAnswer = dto.aiDescription,
            isValid = dto.isCorrect,
        )
    }

    override fun map(dto: ThemeDto): AiThemeModel {
        return AiThemeModel(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            iconUrl = dto.iconUrl,
            detail = dto.detail,
        )
    }

    override fun map(dto: ThemeDetailDto): AiThemeDetailModel {
        return AiThemeDetailModel(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            iconUrl = dto.iconUrl,
            detail = dto.detail,
            content = clearContent(dto.content),
        )
    }

    private fun clearContent(content: String): String {
        return if (
            content.indexOf(YANDEX_GPT_RESPONSE_BLOCK) == 0 &&
            content.lastIndexOf(YANDEX_GPT_RESPONSE_BLOCK) == content.length - YANDEX_GPT_RESPONSE_BLOCK_COUNT
        ) {
            content.removePrefix(YANDEX_GPT_RESPONSE_BLOCK).removeSuffix(YANDEX_GPT_RESPONSE_BLOCK)
        } else {
            content
        }
    }

    private companion object {
        private const val YANDEX_GPT_RESPONSE_BLOCK = "```"
        private const val YANDEX_GPT_RESPONSE_BLOCK_COUNT = YANDEX_GPT_RESPONSE_BLOCK.length
    }
}