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

package com.yugyd.quiz.domain.interactor.section

import com.yugyd.quiz.domain.model.section.ProgressState
import com.yugyd.quiz.domain.model.section.Section
import com.yugyd.quiz.domain.repository.content.QuestSource
import com.yugyd.quiz.domain.repository.user.SectionSource
import com.yugyd.quiz.domain.utils.calculatePercentage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SectionInteractorImpl(
    private val questSource: QuestSource,
    private val sectionSource: SectionSource
) : SectionInteractor {

    override suspend fun getSections(themeId: Int): List<Section> = withContext(Dispatchers.IO) {
        val sectionCount = sectionSource.getSectionCountByTheme(themeId)
        val sections = mapCountToSections(themeId, sectionCount)
        sections.defineLocked()
    }

    private suspend fun mapCountToSections(themeId: Int, sectionCount: Int) =
        List(sectionCount) { index ->
            val sectionId = index.inc()
            getSection(themeId = themeId, sectionId = sectionId)
        }

    private suspend fun getSection(themeId: Int, sectionId: Int) = questSource
        .getQuestIdsBySection(themeId, sectionId)
        .let { questIds ->
            Section(
                id = sectionId,
                questIds = questIds,
                count = questIds.size
            )
        }
        .attachPoint()
        .attachProgressState()

    private suspend fun Section.attachPoint(): Section {
        val questIds = questIds.toIntArray()
        val point = sectionSource.getSectionTotalProgress(questIds)
        return copy(point = point)
    }

    private fun Section.attachProgressState(): Section {
        val progressState = defineLevel(calculatePercentage(point, count))
        return copy(progressState = progressState)
    }

    private fun List<Section>.defineLocked(): List<Section> {
        val latestSection = firstOrNull {
            it.progressState == ProgressState.EMPTY || it.progressState == ProgressState.LOW
        }

        val latestIndex = indexOf(latestSection)

        return mapIndexed { index, section ->
            val progressState = if (index > latestIndex && latestSection != null) {
                ProgressState.LOCKED
            } else {
                section.progressState
            }

            section.copy(progressState = progressState)
        }
    }

    private fun defineLevel(progress: Double) = when {
        progress == NULL_BORDER -> ProgressState.EMPTY
        progress < LOW_BORDER -> ProgressState.LOW
        progress > HIGH_BORDER -> ProgressState.SUCCESS
        else -> ProgressState.MEDIUM
    }

    companion object {
        private const val NULL_BORDER = 0.0
        private const val LOW_BORDER = 50.0
        private const val HIGH_BORDER = 90.0
    }
}
