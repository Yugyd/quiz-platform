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

package com.yugyd.quiz.commonui.model.mode

import com.yugyd.quiz.domain.model.share.Mode
import javax.inject.Inject

class ModeUiMapper @Inject constructor() {
    fun map(model: Mode) = when (model) {
        Mode.ARCADE -> ModeUiModel.ARCADE
        Mode.MARATHON -> ModeUiModel.MARATHON
        Mode.TRAIN -> ModeUiModel.TRAIN
        Mode.ERROR -> ModeUiModel.ERROR
        Mode.NONE -> ModeUiModel.NONE
    }

    fun mapToDomain(model: ModeUiModel) = when (model) {
        ModeUiModel.ARCADE -> Mode.ARCADE
        ModeUiModel.MARATHON -> Mode.MARATHON
        ModeUiModel.TRAIN -> Mode.TRAIN
        ModeUiModel.ERROR -> Mode.ERROR
        ModeUiModel.NONE -> Mode.NONE
    }
}
