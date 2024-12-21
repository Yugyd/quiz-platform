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

package com.yugyd.quiz.domain.courses

import com.yugyd.quiz.domain.courses.model.CourseDetailModel
import com.yugyd.quiz.domain.courses.model.CourseModel

/**
 * Source of in-memory data for courses. Source needs thread-safe access to the data.
 */
internal interface CourseInMemorySource {
    var cachedCourses: List<CourseModel>
    var cachedSubCourses: Map<Int, List<CourseModel>>
    var cachedCurrentCourse: CourseDetailModel?
}
