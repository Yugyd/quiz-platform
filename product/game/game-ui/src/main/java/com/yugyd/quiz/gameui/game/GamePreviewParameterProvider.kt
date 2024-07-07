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

package com.yugyd.quiz.gameui.game

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.yugyd.quiz.gameui.R
import com.yugyd.quiz.gameui.game.model.ConditionUiModel
import com.yugyd.quiz.gameui.game.model.ControlUiModel
import com.yugyd.quiz.gameui.game.model.GameStateUiModel
import com.yugyd.quiz.ui.game.api.model.HighlightUiModel
import com.yugyd.quiz.ui.simplequest.SimpleQuestUiModel
import com.yugyd.quiz.uikit.theme.app_color_negative

internal class GamePreviewParameterProvider :
    PreviewParameterProvider<GameStateUiModel> {

    override val values: Sequence<GameStateUiModel>
        get() = sequenceOf(
            GameStateUiModel(
                control = createControl(),
                quest = createQuest(),
            )
        )

    private fun createControl() = ControlUiModel(
        id = 1,
        countTitle = "1",
        conditionIcon = R.drawable.ic_heart,
        conditionTitle = "",
        conditionTintColor = app_color_negative,
        conditionUiModel = ConditionUiModel.LIFE,
        conditionIsVisible = true,
        progressPercent = 30,
        progressColor = app_color_negative,
        isBanner = true,
    )

    private fun createQuest() = SimpleQuestUiModel(
        quest = "Quest",
        oneAnswer = "One answer",
        twoAnswer = "Two answer",
        threeAnswer = "Three answer",
        fourAnswer = "Four answer",
        answerButtonIsEnabled = false,
        highlight = HighlightUiModel.Default,
    )
}
