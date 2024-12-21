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

package com.yugyd.quiz.ai.connection.api.model

enum class AiConnectionProviderTypeModel(
    val qualifier: String,
    val isNeedFolder: Boolean,
) {
    YANDEX(
        qualifier = "yandex",
        isNeedFolder = true,
    ),
    CHAT_GPT(
        qualifier = "chat-gpt",
        isNeedFolder = false,
    ),
    NONE(
        qualifier = "none",
        isNeedFolder = false,
    );

    companion object {
        fun fromQualifier(qualifier: String) =
            entries.firstOrNull { it.qualifier == qualifier } ?: NONE
    }
}
