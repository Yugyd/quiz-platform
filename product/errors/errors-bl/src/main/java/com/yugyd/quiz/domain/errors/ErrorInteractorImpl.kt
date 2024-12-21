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

package com.yugyd.quiz.domain.errors

import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import com.yugyd.quiz.core.searchutils.QueryFormatRepository
import com.yugyd.quiz.core.searchutils.QueryUrlBuilder
import com.yugyd.quiz.core.searchutils.SearchQuest
import com.yugyd.quiz.domain.aitasks.AiTasksInteractor
import com.yugyd.quiz.domain.api.model.tasks.TaskModel
import com.yugyd.quiz.domain.api.repository.ErrorSource
import com.yugyd.quiz.domain.api.repository.QuestSource
import com.yugyd.quiz.domain.game.api.model.Quest
import com.yugyd.quiz.domain.utils.SeparatorParser
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ErrorInteractorImpl @Inject constructor(
    private val questSource: QuestSource,
    private val errorSource: ErrorSource,
    private val separatorParser: SeparatorParser,
    private val dispatcherProvider: DispatchersProvider,
    private val queryUrlBuilder: QueryUrlBuilder,
    private val queryFormatRepository: QueryFormatRepository,
    private val aiTasksInteractor: AiTasksInteractor,
) : ErrorInteractor {

    override suspend fun getErrorsModels(errors: List<Int>) = withContext(dispatcherProvider.io) {
        val queryFormat = queryFormatRepository.getFormatFromRemoteConfig()
        val errorsModels = questSource.getQuestIdsByErrors(errors.toIntArray())
        mapQuests(errorsModels, queryFormat)
    }

    override suspend fun getErrorModelsFromAiTasks(
        themeId: Int,
        errors: List<Int>,
    ) = withContext(dispatcherProvider.io) {
        val queryFormat = queryFormatRepository.getFormatFromRemoteConfig()
        val errorsModels = aiTasksInteractor
            .getQuests(aiThemeId = themeId)
            .filter { it.id in errors }
        mapQuests(errorsModels, queryFormat)
    }

    override suspend fun getErrors() = withContext(dispatcherProvider.io) {
        errorSource.getErrors()
    }

    override suspend fun isHaveErrors() = withContext(dispatcherProvider.io) {
        errorSource.isHaveErrors()
    }

    override suspend fun addErrors(errors: List<Int>) = withContext(dispatcherProvider.io) {
        errorSource.addErrors(errors)
    }

    override suspend fun removeErrors(errors: List<Int>) = withContext(dispatcherProvider.io) {
        errorSource.removeErrors(errors)
    }

    private fun mapQuests(quests: List<Quest>, queryFormat: String) = quests
        .map(separatorParser::parseErrorQuest)
        .map { it.toErrorModel(queryFormat) }

    private fun Quest.toErrorModel(queryFormat: String) = TaskModel(
        id = id,
        quest = quest,
        trueAnswer = trueAnswer,
        queryLink = queryUrlBuilder.buildUrl(this.toSearchQuest(), queryFormat),
    )

    private fun Quest.toSearchQuest() = SearchQuest(
        quest = quest,
        trueAnswer = trueAnswer,
    )
}
