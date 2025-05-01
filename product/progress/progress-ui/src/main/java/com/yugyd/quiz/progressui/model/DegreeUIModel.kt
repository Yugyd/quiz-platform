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

package com.yugyd.quiz.progressui.model

import androidx.annotation.StringRes
import com.yugyd.quiz.progressui.R

internal enum class DegreeUIModel(@StringRes val title: Int) {
    SCHOOLBOY(title = R.string.progress_title_level_schoolboy),
    AMATEUR(title = R.string.progress_title_level_amateur),
    STUDENT(title = R.string.progress_title_level_student),
    MASTER(title = R.string.progress_title_level_master),
    POSTGRADUATE(title = R.string.progress_title_level_postgraduate),
    CANDIDATE(title = R.string.progress_title_level_candidate),
    DOCTOR(title = R.string.progress_title_level_doctor),
    PROFESSOR(title = R.string.progress_title_level_professor),
    ACADEMIC(title = R.string.progress_title_level_academic)
}
