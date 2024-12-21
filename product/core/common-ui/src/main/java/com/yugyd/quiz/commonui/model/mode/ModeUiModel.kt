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

import androidx.annotation.StringRes
import com.yugyd.quiz.uikit.R as uiKitR

enum class ModeUiModel(@StringRes val title: Int) {
    ARCADE(uiKitR.string.title_arcade),
    TRAIN(uiKitR.string.title_train),
    ERROR(uiKitR.string.title_correct),
    FAVORITE(uiKitR.string.title_favorites),
    AI_TASKS(uiKitR.string.title_ai_tasks),
    NONE(uiKitR.string.title_unknown)
}
