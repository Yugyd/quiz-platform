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

package com.yugyd.quiz.domain.questai

import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import com.yugyd.quiz.domain.questai.model.AiQuestResultModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AiQuestInteractorImpl @Inject constructor(
    private val dispatcherProvider: DispatchersProvider,
) : AiQuestInteractor {

    override suspend fun checkAnswer(
        userAnswer: String,
        trueAnswer: String,
    ) = withContext(dispatcherProvider.io) {
        delay(REMOTE_DELAY)
        // TODO Replace to api call and cache result
        AiQuestResultModel(
            aiTrueAnswer = trueAnswer,
            isValid = userAnswer.equals(trueAnswer, ignoreCase = true),
        )
    }

    private companion object {
        private const val REMOTE_DELAY = 3000L
    }
}
