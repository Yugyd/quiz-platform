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

package com.yugyd.quiz.domain.tasks

import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import com.yugyd.quiz.core.searchutils.QueryFormatRepository
import com.yugyd.quiz.core.searchutils.QueryUrlBuilder
import com.yugyd.quiz.core.searchutils.SearchQuest
import com.yugyd.quiz.domain.api.repository.QuestSource
import com.yugyd.quiz.domain.game.api.model.Quest
import com.yugyd.quiz.domain.tasks.model.TaskModel
import com.yugyd.quiz.domain.utils.SeparatorParser
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class TasksInteractorImpl @Inject constructor(
    private val questSource: QuestSource,
    private val separatorParser: SeparatorParser,
    private val dispatcherProvider: DispatchersProvider,
    private val queryUrlBuilder: QueryUrlBuilder,
    private val queryFormatRepository: QueryFormatRepository,
) : TasksInteractor {

    override suspend fun getTaskModels(): List<TaskModel> = withContext(dispatcherProvider.io) {
        val queryFormat = queryFormatRepository.getFormatFromRemoteConfig()
        val quests = questSource.getQuests()
        mapQuests(quests, queryFormat)
    }

    private fun mapQuests(quests: List<Quest>, queryFormat: String) = quests
        .map(separatorParser::parseErrorQuest)
        .map { it.toTaskModel(queryFormat) }

    private fun Quest.toTaskModel(queryFormat: String) = TaskModel(
        id = id,
        quest = quest,
        trueAnswer = trueAnswer,
        queryLink = queryUrlBuilder.buildUrl(this.toSearchQuest(), queryFormat),
        complexity = complexity,
    )

    private fun Quest.toSearchQuest() = SearchQuest(
        quest = quest,
        trueAnswer = trueAnswer,
    )
}

