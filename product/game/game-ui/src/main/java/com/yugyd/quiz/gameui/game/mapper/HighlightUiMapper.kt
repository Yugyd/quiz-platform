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

import com.yugyd.quiz.domain.game.api.model.HighlightModel
import com.yugyd.quiz.ui.game.api.model.HighlightUiModel
import javax.inject.Inject

internal class HighlightUiMapper @Inject constructor() {

    fun map(model: HighlightModel) = when (model.state) {
        HighlightModel.State.TRUE -> {
            HighlightUiModel.True(
                trueAnswerIndexes = model.selectedAnswerIndex
            )
        }

        HighlightModel.State.FALSE -> {
            HighlightUiModel.False(
                trueAnswerIndexes = model.trueAnswerIndexes,
                falseIndexes = model.selectedAnswerIndex
            )
        }

        HighlightModel.State.NONE -> {
            HighlightUiModel.Default
        }
    }
}
