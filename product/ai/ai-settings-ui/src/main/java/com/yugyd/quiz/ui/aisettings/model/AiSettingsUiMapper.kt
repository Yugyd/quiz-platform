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

package com.yugyd.quiz.ui.aisettings.model

import android.content.Context
import com.yugyd.quiz.ai.connection.api.model.AiConnectionModel
import com.yugyd.quiz.domain.aiconnection.model.AiTermCache
import com.yugyd.quiz.ui.aisettings.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class AiSettingsUiMapper @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun map(models: List<AiConnectionModel>) = models.map {
        AiConnectionUiModel(
            id = it.id,
            name = it.name,
            status = if (it.isActive) {
                context.getString(R.string.ai_settings_title_ai_connected)
            } else {
                null
            },
            isActive = it.isActive,
        )
    }

    fun mapAiTerms(aiTerms: List<AiTermCache>) = aiTerms.map {
        it.daysCount
    }
}
