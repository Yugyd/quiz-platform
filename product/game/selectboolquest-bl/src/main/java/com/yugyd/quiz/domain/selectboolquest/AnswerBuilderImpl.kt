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

package com.yugyd.quiz.domain.selectboolquest

import android.content.Context
import com.yugyd.quiz.domain.selectbool.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class AnswerBuilderImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : AnswerBuilder {

    private val trueAnswer by lazy {
        context.getString(R.string.select_bool_quest_answer_true)
    }

    private val falseAnswer by lazy {
        context.getString(R.string.select_bool_quest_answer_false)
    }

    override fun buildAnswers() = listOf(trueAnswer, falseAnswer)

    override fun buildTrueAnswer(rawTrueAnswer: String): String {
        return when (rawTrueAnswer.toInt()) {
            0 -> falseAnswer
            1 -> trueAnswer
            else -> throw IllegalArgumentException()
        }
    }
}
