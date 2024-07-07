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

package com.yugyd.quiz.domain.utils

import com.yugyd.quiz.domain.game.api.model.Quest
import javax.inject.Inject

class SeparatorParser @Inject constructor() {

    fun parse(model: Quest): Quest {
        var sourceQuest = model.quest

        if (sourceQuest.contains(QUALIFIER_COMMON_TEXT)) {
            sourceQuest = sourceQuest.replace(QUALIFIER_COMMON_TEXT, "")
        }

        if (sourceQuest.contains(QUALIFIER_SEPARATOR)) {
            sourceQuest = sourceQuest.replace(QUALIFIER_SEPARATOR, System.lineSeparator())
        }

        return model.copy(quest = sourceQuest)
    }

    fun parseErrorQuest(model: Quest): Quest {
        var sourceQuest = model.quest
        sourceQuest = sourceQuest.replace(QUALIFIER_COMMON_TEXT, "")
        sourceQuest = sourceQuest.replace(QUALIFIER_SEPARATOR, "")
        return model.copy(quest = sourceQuest)
    }

    companion object {
        private const val QUALIFIER_COMMON_TEXT = "&"
        private const val QUALIFIER_SEPARATOR = "#"
    }
}