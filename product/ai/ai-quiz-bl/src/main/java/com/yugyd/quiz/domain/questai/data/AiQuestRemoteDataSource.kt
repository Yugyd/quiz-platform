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

package com.yugyd.quiz.domain.questai.data

import com.yugyd.quiz.core.result
import com.yugyd.quiz.domain.questai.AiQuestRemoteSource
import com.yugyd.quiz.domain.questai.data.dto.VerifyTaskRequest
import com.yugyd.quiz.domain.questai.data.mapper.AiQuestMapper
import com.yugyd.quiz.domain.questai.model.AiTaskModel
import com.yugyd.quiz.domain.questai.model.AiThemeDetailModel
import com.yugyd.quiz.domain.questai.model.AiThemeModel
import com.yugyd.quiz.domain.questai.model.AiVerifyTaskModel
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class InvalidContentException(override val message: String?) : RuntimeException(message)

class ContentNotFoundException(override val message: String?) : RuntimeException(message)

class AiQuestRemoteDataSource @Inject constructor(
    private val questApi: QuizPlatformApi,
    private val mapper: AiQuestMapper,
) : AiQuestRemoteSource {

    override suspend fun getTasks(themeId: Int): List<AiTaskModel> {
        return processResponse(
            apiRequest = {
                questApi.getTasks(themeId = themeId)
            },
            mapper = {
                it.map(mapper::map)
            },
        )
    }

    override suspend fun verifyTask(
        quest: String,
        userAnswer: String,
        trueAnswer: String
    ): AiVerifyTaskModel {
        return processResponse(
            apiRequest = {
                questApi.verifyTask(
                    answer = VerifyTaskRequest(
                        quest = quest,
                        userAnswer = userAnswer,
                        trueAnswer = trueAnswer,
                    ),
                )
            },
            mapper = mapper::map,
        )
    }

    override suspend fun getThemes(parentThemeId: Int?): List<AiThemeModel> {
        return processResponse(
            apiRequest = {
                questApi.getThemes(parentThemeId = parentThemeId)
            },
            mapper = {
                it.map(mapper::map)
            },
        )
    }

    override suspend fun getThemeDetail(themeId: Int): AiThemeDetailModel {
        return processResponse(
            apiRequest = {
                questApi.getThemeDetail(themeId = themeId)
            },
            mapper = mapper::map,
        )
    }

    private suspend fun <T, R> processResponse(
        apiRequest: suspend () -> Response<T>,
        mapper: (T) -> R,
    ): R {
        return result {
            val response = apiRequest()

            if (response.isSuccessful && response.body() != null) {
                response
                    .body()!!
                    .let(mapper)
            } else {
                when (response.code()) {
                    INVALID_CONTENT -> throw InvalidContentException(response.message())
                    CONTENT_NOT_FOUND -> throw ContentNotFoundException(response.message())
                    else -> throw HttpException(response)
                }
            }
        }
            .getOrThrow()
    }

    private companion object {
        private const val INVALID_CONTENT = 400
        private const val CONTENT_NOT_FOUND = 404
    }
}
