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

package com.yugyd.quiz.domain.interactor.error

import com.yugyd.quiz.domain.model.data.Quest
import com.yugyd.quiz.domain.model.errorlist.ErrorModel
import com.yugyd.quiz.domain.repository.content.QuestSource
import com.yugyd.quiz.domain.repository.user.ErrorSource
import com.yugyd.quiz.domain.utils.SeparatorParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ErrorInteractorImpl(
    private val questSource: QuestSource,
    private val errorSource: ErrorSource,
    private val separatorParser: SeparatorParser
) : ErrorInteractor {

    override suspend fun getErrorsModels(errors: List<Int>) = withContext(Dispatchers.IO) {
        questSource
            .getQuestIdsByErrors(errors.toIntArray())
            .let(::mapQuests)
    }

    override suspend fun getErrors() = withContext(Dispatchers.IO) {
        errorSource.getErrors()
    }

    override suspend fun isHaveErrors() = withContext(Dispatchers.IO) {
        errorSource.isHaveErrors()
    }

    override suspend fun addErrors(errors: List<Int>) = withContext(Dispatchers.IO) {
        errorSource.addErrors(errors)
    }

    override suspend fun removeErrors(errors: List<Int>) = withContext(Dispatchers.IO) {
        errorSource.removeErrors(errors)
    }

    private fun mapQuests(quests: List<Quest>) = quests
        .map(separatorParser::parseErrorQuest)
        .map { it.toErrorModel() }

    private fun Quest.toErrorModel() = ErrorModel(
        id = id,
        quest = quest,
        trueAnswer = trueAnswer,
        queryLink = "$GOOGLE_SEARCH_URL$quest$trueAnswer"
    )

    companion object {
        private const val GOOGLE_SEARCH_URL = "https://www.google.ru/search?q="
    }
}
