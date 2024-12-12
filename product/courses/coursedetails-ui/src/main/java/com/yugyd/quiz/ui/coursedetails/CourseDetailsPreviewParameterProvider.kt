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

package com.yugyd.quiz.ui.coursedetails

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.yugyd.quiz.domain.courses.model.CourseDetailModel

internal class CourseDetailsPreviewParameterProvider :
    PreviewParameterProvider<List<CourseDetailModel>> {

    companion object {
        private const val TEST_ITEM_COUNT = 5
    }

    override val values: Sequence<List<CourseDetailModel>>
        get() = sequenceOf(
            buildList {
                repeat(TEST_ITEM_COUNT) {
                    add(
                        CourseDetailModel(
                            id = it,
                            name = "Name $it",
                            description = "Description $it",
                            icon = null,
                            isDetail = true,
                            parentCourseId = 99,
                            content = generateTestMd("Name $it"),
                        )
                    )
                }
            }
        )

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
}
