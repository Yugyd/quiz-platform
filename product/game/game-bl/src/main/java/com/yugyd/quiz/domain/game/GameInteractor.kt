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

package com.yugyd.quiz.domain.game

import com.yugyd.quiz.domain.api.model.payload.GameEndPayload
import com.yugyd.quiz.domain.api.model.payload.GamePayload
import com.yugyd.quiz.domain.game.api.BaseQuestDomainModel
import com.yugyd.quiz.domain.game.api.model.HighlightModel
import com.yugyd.quiz.domain.game.model.GameState

interface GameInteractor {

    suspend fun startGame(payload: GamePayload): GameState

    suspend fun continueGame(): GameState

    suspend fun resultAnswer(
        quest: BaseQuestDomainModel,
        selectedUserAnswers: Set<String>,
        userAnswer: String,
    ): HighlightModel

    suspend fun finishGame(): GameEndPayload

    suspend fun firstFinishGame()

    suspend fun onUserEarnedReward()
}

