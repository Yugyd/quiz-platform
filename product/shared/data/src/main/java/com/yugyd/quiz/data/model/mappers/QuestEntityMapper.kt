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

package com.yugyd.quiz.data.model.mappers

import com.yugyd.quiz.data.crypto.CryptoHelper
import com.yugyd.quiz.data.model.QuestEntity
import com.yugyd.quiz.domain.api.model.Quest
import javax.inject.Inject

class QuestEntityMapper @Inject constructor(private val cryptoHelper: CryptoHelper) {

    fun mapQuestToDomain(entity: QuestEntity) = entity.run {
        Quest(
            id = id,
            quest = cryptoHelper.decrypt(quest),
            trueAnswer = cryptoHelper.decrypt(trueAnswer),
            answer2 = cryptoHelper.decrypt(answer2),
            answer3 = cryptoHelper.decrypt(answer3),
            answer4 = cryptoHelper.decrypt(answer4),
            answer5 = cryptoHelper.decrypt(answer5),
            answer6 = cryptoHelper.decrypt(answer6),
            answer7 = cryptoHelper.decrypt(answer7),
            answer8 = cryptoHelper.decrypt(answer8),
            complexity = complexity,
            category = category,
            section = section
        )
    }

    fun mapQuestToEntity(model: Quest) = model.run {
        QuestEntity(
            id = id,
            quest = cryptoHelper.encrypt(quest),
            trueAnswer = cryptoHelper.encrypt(trueAnswer),
            answer2 = cryptoHelper.encrypt(answer2),
            answer3 = cryptoHelper.encrypt(answer3),
            answer4 = cryptoHelper.encrypt(answer4),
            answer5 = cryptoHelper.encrypt(answer5),
            answer6 = cryptoHelper.encrypt(answer6),
            answer7 = cryptoHelper.encrypt(answer7),
            answer8 = cryptoHelper.encrypt(answer8),
            complexity = complexity,
            category = category,
            section = section,
        )
    }
}
