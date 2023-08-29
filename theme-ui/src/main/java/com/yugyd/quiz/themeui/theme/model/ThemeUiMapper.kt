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

package com.yugyd.quiz.themeui.theme.model

import android.net.Uri
import com.yugyd.quiz.commonui.utils.ProgressColorUtils
import com.yugyd.quiz.domain.model.data.Theme
import com.yugyd.quiz.domain.utils.percent
import javax.inject.Inject

class ThemeUiMapper @Inject constructor(
    private val progressColorUtils: ProgressColorUtils
) {

    fun map(model: Theme) = model.run {
        val progressPercent = percent(progress, count)
        ThemeUiModel(
            id = id,
            title = name,
            subtitle = info,
            imageUri = Uri.parse("$ASSETS_PATH$image"),
            progressPercent = progressPercent,
            progressColor = progressColorUtils.getProgressColor(progressPercent),
            record = progress
        )
    }

    companion object {
        private const val ASSETS_PATH = "file:///android_asset/"
    }
}
