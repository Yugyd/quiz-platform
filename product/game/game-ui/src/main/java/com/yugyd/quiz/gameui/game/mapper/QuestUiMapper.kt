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

import com.yugyd.quiz.domain.api.model.game.QuestModel
import com.yugyd.quiz.gameui.game.model.QuestUiModel
import javax.inject.Inject

class QuestUiMapper @Inject constructor() {

    fun map(model: QuestModel) = model.run {
        QuestUiModel(
            id = id,
            quest = quest,
            oneAnswer = answers[ONE_ANSWER],
            twoAnswer = answers[TWO_ANSWER],
            threeAnswer = answers[THREE_ANSWER],
            fourAnswer = answers[FOUR_ANSWER]
        )
    }

    companion object {
        private const val ONE_ANSWER = 0
        private const val TWO_ANSWER = 1
        private const val THREE_ANSWER = 2
        private const val FOUR_ANSWER = 3
    }
}
