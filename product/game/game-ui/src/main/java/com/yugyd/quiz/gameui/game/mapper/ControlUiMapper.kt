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

package com.yugyd.quiz.gameui.game.mapper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.ui.graphics.Color
import com.yugyd.quiz.commonui.utils.ProgressColorUtils
import com.yugyd.quiz.core.percent
import com.yugyd.quiz.domain.api.model.Mode
import com.yugyd.quiz.domain.api.model.game.ControlModel
import com.yugyd.quiz.gameui.game.model.ConditionUiModel
import com.yugyd.quiz.gameui.game.model.ControlUiModel
import javax.inject.Inject

internal class ControlUiMapper @Inject constructor(private val progressColorUtils: ProgressColorUtils) {

    private val ControlModel.percentProgress: Int
        get() = percent(steep, questCount)

    private val ControlModel.colorProgress: Color
        get() = progressColorUtils.getProgressColor(percentProgress)

    fun map(model: ControlModel) = model.run {
        val conditionType = when (model.mode) {
            Mode.TRAIN -> ConditionUiModel.HIDE
            else -> ConditionUiModel.LIFE
        }

        val conditionIcon = if (condition == MIN_CONDITION) {
            Icons.Filled.Favorite
        } else {
            Icons.Outlined.Favorite
        }

        ControlUiModel(
            countTitle = point.toString(),
            progressPercent = percentProgress,
            progressColor = colorProgress,
            conditionUiModel = conditionType,
            conditionTitle = condition.toString(),
            conditionIcon = conditionIcon
        )
    }

    companion object {
        private const val MIN_CONDITION = 1
    }
}
