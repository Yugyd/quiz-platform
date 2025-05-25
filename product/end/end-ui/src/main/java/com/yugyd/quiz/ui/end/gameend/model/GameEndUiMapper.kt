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

package com.yugyd.quiz.ui.end.gameend.model

import android.content.Context
import com.yugyd.quiz.commonui.utils.ProgressColorUtils
import com.yugyd.quiz.core.calculatePercentageToRounded
import com.yugyd.quiz.domain.api.model.Mode
import com.yugyd.quiz.domain.api.model.payload.GameEndPayload
import com.yugyd.quiz.ui.end.R
import com.yugyd.quiz.ui.end.gameend.GameEndView.State
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import com.yugyd.quiz.uikit.R as uiKitR

internal class GameEndUiMapper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val progressColorUtils: ProgressColorUtils
) {

    fun map(
        payload: GameEndPayload,
        themeTitle: String,
        isAdFeatureEnabled: Boolean
    ): State {
        val mappedThemeTitle = when (payload.mode) {
            Mode.ARCADE, Mode.TRAIN, Mode.AI_TASKS -> themeTitle
            Mode.ERROR -> context.getString(R.string.end_title_work_error)
            Mode.FAVORITE -> context.getString(R.string.end_title_work_favorites)
            Mode.NONE -> ""
        }

        val progress = calculatePercentageToRounded(payload.point, payload.count)

        val progressTitle = context.getString(
            uiKitR.string.ds_format_record_progress,
            payload.point,
            payload.count
        )

        return State(
            payload = payload,
            themeTitle = mappedThemeTitle,
            progress = progress,
            isErrorVisible = payload.errorQuestIds.isNotEmpty(),
            progressTitle = progressTitle,
            progressColor = progressColorUtils.getProgressColor(progress),
            isAdFeatureEnabled = isAdFeatureEnabled,
            isLoading = false
        )
    }
}
