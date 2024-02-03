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

import com.yugyd.quiz.domain.api.model.Degree
import com.yugyd.quiz.domain.api.model.Degree.ACADEMIC
import com.yugyd.quiz.domain.api.model.Degree.AMATEUR
import com.yugyd.quiz.domain.api.model.Degree.CANDIDATE
import com.yugyd.quiz.domain.api.model.Degree.DOCTOR
import com.yugyd.quiz.domain.api.model.Degree.MASTER
import com.yugyd.quiz.domain.api.model.Degree.POSTGRADUATE
import com.yugyd.quiz.domain.api.model.Degree.PROFESSOR
import com.yugyd.quiz.domain.api.model.Degree.SCHOOLBOY
import com.yugyd.quiz.domain.api.model.Degree.STUDENT
import javax.inject.Inject

internal class DegreeUiMapper @Inject constructor() {
    fun map(model: Degree) = when (model) {
        SCHOOLBOY -> DegreeUIModel.SCHOOLBOY
        AMATEUR -> DegreeUIModel.AMATEUR
        STUDENT -> DegreeUIModel.STUDENT
        MASTER -> DegreeUIModel.MASTER
        POSTGRADUATE -> DegreeUIModel.POSTGRADUATE
        CANDIDATE -> DegreeUIModel.CANDIDATE
        DOCTOR -> DegreeUIModel.DOCTOR
        PROFESSOR -> DegreeUIModel.PROFESSOR
        ACADEMIC -> DegreeUIModel.ACADEMIC
    }
}
