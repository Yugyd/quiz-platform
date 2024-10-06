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

package com.yugyd.quiz.gameui.game.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.yugyd.quiz.gameui.game.model.ConditionUiModel.NONE
import com.yugyd.quiz.uikit.theme.app_color_negative

internal data class ControlUiModel(
    val id: Int = 0,
    val countTitle: String = "",
    val conditionTitle: String = "",
    val conditionIcon: ImageVector = Icons.Filled.Favorite,
    val conditionTintColor: Color = app_color_negative,
    val conditionUiModel: ConditionUiModel = NONE,
    val conditionIsVisible: Boolean = true,
    val progressPercent: Int = 0,
    val progressColor: Color = app_color_negative,
    val isBanner: Boolean = true,
)
