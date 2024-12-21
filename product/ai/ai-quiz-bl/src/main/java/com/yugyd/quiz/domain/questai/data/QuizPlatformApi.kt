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

package com.yugyd.quiz.domain.questai.data

import com.yugyd.quiz.domain.questai.data.dto.TaskDto
import com.yugyd.quiz.domain.questai.data.dto.ThemeDetailDto
import com.yugyd.quiz.domain.questai.data.dto.ThemeDto
import com.yugyd.quiz.domain.questai.data.dto.VerifyTaskDto
import com.yugyd.quiz.domain.questai.data.dto.VerifyTaskRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface QuizPlatformApi {

    @GET("tasks/{themeId}")
    suspend fun getTasks(
        @Path("themeId") themeId: Int,
    ): Response<List<TaskDto>>

    @POST("tasks/verification")
    suspend fun verifyTask(
        @Body answer: VerifyTaskRequest,
    ): Response<VerifyTaskDto>

    @GET("themes")
    suspend fun getThemes(
        @Query("parentThemeId") parentThemeId: Int? = null,
    ): Response<List<ThemeDto>>

    @GET("themes/{themeId}")
    suspend fun getThemeDetail(
        @Path("themeId") themeId: Int,
        @Query("recreate") recreate: Boolean = true,
    ): Response<ThemeDetailDto>
}

