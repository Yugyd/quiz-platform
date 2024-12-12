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
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CourseInteractorImpl @Inject constructor(
    private val dispatcherProvider: DispatchersProvider,
) : CourseInteractor {

    private val mockCourses by lazy {
        buildList {
            repeat(10) {
                add(
                    CourseModel(
                        id = it,
                        name = "Course $it",
                        description = "Description $it",
                        icon = null,
                        isDetail = false,
                        parentCourseId = null,
                    )
                )
            }
        }
    }

    private val subCourses by lazy {
        mockCourses.associate { parent ->
            parent.id to buildList {
                repeat(10) {
                    add(
                        CourseModel(
                            id = it,
                            name = "Sub course ${parent.id} - $it",
                            description = "Sub course description ${parent.id} - $it",
                            icon = null,
                            isDetail = true,
                            parentCourseId = it
                        )
                    )
                }
            }
        }
    }

    private var currentCourse: CourseDetailModel =
        subCourses[0]?.first(CourseModel::isDetail)!!.let {
            CourseDetailModel(
                id = it.id,
                name = it.name,
                description = it.description,
                content = generateTestMd(it.name),
                icon = it.icon,
                parentCourseId = it.parentCourseId,
                isDetail = it.isDetail,
            )
        }

    override suspend fun getCourses() = withContext(dispatcherProvider.io) {
        delay(REMOTE_DELAY)
        mockCourses
    }

    override suspend fun getCourses(parentCourseId: Int) = withContext(dispatcherProvider.io) {
        delay(REMOTE_DELAY)
        subCourses[parentCourseId]!!
    }

    override suspend fun getCurrentCourse() = withContext(dispatcherProvider.io) {
        delay(DATABASE_DELAY)
        CourseModel(
            id = currentCourse.id,
            name = currentCourse.name,
            description = currentCourse.description,
            icon = currentCourse.icon,
            isDetail = currentCourse.isDetail,
            parentCourseId = currentCourse.parentCourseId,
        )
    }

    override suspend fun setCurrentCourse(courseModel: CourseDetailModel) {
        delay(DATABASE_DELAY)
        currentCourse = courseModel
    }

    override suspend fun getCourseDetails(courseId: Int) = withContext(dispatcherProvider.io) {
        delay(REMOTE_DELAY)
        subCourses.values.flatten().first { it.id == courseId }.let {
            CourseDetailModel(
                id = it.id,
                name = it.name,
                description = it.description,
                content = generateTestMd(it.name),
                icon = it.icon,
                parentCourseId = it.parentCourseId,
                isDetail = it.isDetail,
            )
        }
    }

    private fun generateTestMd(name: String): String {
        return """
            # $name
            
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed non risus.
            
            # Contributions

            [Guide](docs/CONTRIBUTION.md)

            # Build

            ## Build debug

            - `./gradlew clean assembleDevDebug`
            
            ## Code integration

            * Switch the `isProductFlavorFilterEnabled` property to `false` in the
              [BuildTypeAndroidApplicationPlugin.kt](build-logic/convention/src/main/kotlin/com/yugyd/buildlogic/convention/buildtype/BuildTypeAndroidApplicationPlugin.kt)
            * Switch the `IS_BASED_ON_PLATFORM_APP` property to `true` in the [build.gradle](app/build.gradle)
              file.
              
            # Code snippet
            
            ```kotlin
            fun main() {
                println("Hello, World!")
            }
            ```
            
            # License

            ```
               Copyright 2023 Roman Likhachev
            
               Licensed under the Apache License, Version 2.0 (the "License");
               you may not use this file except in compliance with the License.
               You may obtain a copy of the License at
            
                   http://www.apache.org/licenses/LICENSE-2.0
            
               Unless required by applicable law or agreed to in writing, software
               distributed under the License is distributed on an "AS IS" BASIS,
               WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
               See the License for the specific language governing permissions and
               limitations under the License.
            ```
        """.trimIndent()
    }

    private companion object {
        private const val REMOTE_DELAY = 3000L
        private const val DATABASE_DELAY = 300L
    }
}
