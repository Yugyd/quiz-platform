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

package com.yugyd.quiz.domain.courses

import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import com.yugyd.quiz.domain.courses.model.CourseDetailModel
import com.yugyd.quiz.domain.courses.model.CourseModel
import com.yugyd.quiz.domain.questai.AiQuestInteractor
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CourseInteractorImpl @Inject constructor(
    private val aiQuestInteractor: AiQuestInteractor,
    private val courseInMemorySource: CourseInMemorySource,
    private val dispatcherProvider: DispatchersProvider,
) : CourseInteractor {

    private val inMemoryCacheMutex = Mutex()

    override suspend fun getCourses() = withContext(dispatcherProvider.io) {
        val themes = aiQuestInteractor.getThemes()
            .map {
                CourseModel(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    icon = it.iconUrl,
                    isDetail = it.detail ?: false,
                    parentCourseId = null,
                )
            }

        inMemoryCacheMutex.withLock {
            courseInMemorySource.cachedCourses = themes
            courseInMemorySource.cachedCourses
        }
    }

    override suspend fun getCourses(parentCourseId: Int) = withContext(dispatcherProvider.io) {
        val themes = aiQuestInteractor
            .getThemes(parentCourseId)
            .map {
                CourseModel(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    icon = it.iconUrl,
                    isDetail = it.detail ?: false,
                    parentCourseId = parentCourseId,
                )
            }

        inMemoryCacheMutex.withLock {
            courseInMemorySource.cachedSubCourses += (parentCourseId to themes)
            courseInMemorySource.cachedSubCourses[parentCourseId].orEmpty()
        }
    }

    override suspend fun getCurrentCourse() = withContext(dispatcherProvider.io) {
        inMemoryCacheMutex.withLock {
            courseInMemorySource.cachedCurrentCourse
        }
    }

    override suspend fun setCurrentCourse(courseModel: CourseDetailModel) {
        inMemoryCacheMutex.withLock {
            courseInMemorySource.cachedCurrentCourse = courseModel
        }
    }

    override suspend fun getCourseDetails(courseId: Int) = withContext(dispatcherProvider.io) {
        aiQuestInteractor.getThemeDetail(courseId)
            .let {
                CourseDetailModel(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    icon = it.iconUrl,
                    isDetail = it.detail,
                    content = it.content,
                    parentCourseId = null,
                )
            }
    }
}
