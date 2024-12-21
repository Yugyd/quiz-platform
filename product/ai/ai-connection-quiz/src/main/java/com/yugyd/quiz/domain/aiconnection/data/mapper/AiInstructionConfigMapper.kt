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

package com.yugyd.quiz.domain.aiconnection.data.mapper

import com.yugyd.quiz.ai.connection.api.model.AiConnectionProviderTypeModel
import com.yugyd.quiz.domain.aiconnection.model.AiInstructionConfig
import com.yugyd.quiz.domain.aiconnection.data.entity.AiInstructionConfigDto
import javax.inject.Inject

class AiInstructionConfigMapper @Inject internal constructor() {

    fun map(dto: AiInstructionConfigDto): AiInstructionConfig = dto.run {
        return AiInstructionConfig(
            id = AiConnectionProviderTypeModel.fromQualifier(id),
            text = text,
            url = url,
        )
    }
}
