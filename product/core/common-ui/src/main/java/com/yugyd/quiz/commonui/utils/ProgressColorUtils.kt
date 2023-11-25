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

package com.yugyd.quiz.commonui.utils

import androidx.compose.ui.graphics.Color
import com.yugyd.quiz.uikit.theme.app_color_negative
import com.yugyd.quiz.uikit.theme.app_color_neutral
import com.yugyd.quiz.uikit.theme.app_color_positive
import javax.inject.Inject

class ProgressColorUtils @Inject constructor() {

    fun getProgressColor(percent: Int): Color = when {
        percent < MEDIUM_LEVEL_PROGRESS -> app_color_negative
        percent < HIGH_LEVEL_PROGRESS -> app_color_neutral
        else -> app_color_positive
    }

    companion object {
        private const val MEDIUM_LEVEL_PROGRESS = 50
        private const val HIGH_LEVEL_PROGRESS = 90
    }
}
