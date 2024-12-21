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

package com.yugyd.quiz.domain.questai

import com.yugyd.quiz.domain.questai.model.AiTaskModel
import com.yugyd.quiz.domain.questai.model.AiThemeDetailModel
import com.yugyd.quiz.domain.questai.model.AiThemeModel
import com.yugyd.quiz.domain.questai.model.AiVerifyTaskModel

interface AiQuestInteractor {

    suspend fun getTasks(themeId: Int): List<AiTaskModel>

    suspend fun verifyTask(
        quest: String,
        userAnswer: String,
        trueAnswer: String,
    ): AiVerifyTaskModel

    suspend fun getThemes(parentThemeId: Int? = null): List<AiThemeModel>

    suspend fun getThemeDetail(themeId: Int): AiThemeDetailModel
}
