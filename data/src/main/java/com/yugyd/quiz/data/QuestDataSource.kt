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

package com.yugyd.quiz.data

import com.yugyd.quiz.data.database.dao.QuestDao
import com.yugyd.quiz.data.model.QuestComplexitySubsetEntity
import com.yugyd.quiz.data.model.mappers.QuestEntityMapper
import com.yugyd.quiz.domain.repository.content.QuestSource

class QuestDataSource(
    private val questDao: QuestDao,
    private val questEntityMapper: QuestEntityMapper
) : QuestSource {

    override suspend fun getQuest(id: Int) = questDao
        .getQuestById(id)
        .let(questEntityMapper::mapQuestToDomain)

    override suspend fun getQuestIds(themeId: Int, isSort: Boolean) =
        if (isSort) {
            questDao.getComplexityQuestByTheme(themeId).sortedAndMapped()
        } else {
            questDao.getIdsByTheme(themeId).shuffled()
        }

    override suspend fun getQuestIdsBySection(themeId: Int, sectionId: Int, isSort: Boolean) =
        if (isSort) {
            questDao.getComplexityQuestBySection(themeId, sectionId).sortedAndMapped()
        } else {
            questDao.getIdsBySection(themeId, sectionId).shuffled()
        }

    override suspend fun getQuestIdsByErrors(errorIds: IntArray) = questDao
        .loadAllByIds(errorIds)
        .map(questEntityMapper::mapQuestToDomain)

    private fun List<QuestComplexitySubsetEntity>.sortedAndMapped() = shuffled()
        .sortedBy(QuestComplexitySubsetEntity::complexity)
        .map(QuestComplexitySubsetEntity::questId)
}
