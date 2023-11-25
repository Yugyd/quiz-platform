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

package com.yugyd.quiz.domain.api.model.payload

import android.os.Parcelable
import com.yugyd.quiz.domain.api.model.Mode
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameEndPayload(
    val mode: Mode = Mode.NONE,
    val themeId: Int? = null,
    val oldRecord: Int = 0,
    val point: Int = 0,
    val count: Int = 0,
    val errorQuestIds: List<Int> = emptyList(),
    val isRewardedSuccess: Boolean = false,
    val isBlockedInterstitial: Boolean = false
) : Parcelable
