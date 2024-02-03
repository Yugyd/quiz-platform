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

package com.yugyd.quiz.domain.progress

import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import com.yugyd.quiz.core.percent
import com.yugyd.quiz.domain.api.model.Degree
import com.yugyd.quiz.domain.api.model.Record
import com.yugyd.quiz.domain.api.model.Theme
import com.yugyd.quiz.domain.api.model.progress.Progress
import com.yugyd.quiz.domain.api.model.progress.TotalProgress
import com.yugyd.quiz.domain.api.model.recordModeCount
import com.yugyd.quiz.domain.api.model.recordModes
import com.yugyd.quiz.domain.api.model.specificprogress.ModeProgress
import com.yugyd.quiz.domain.api.repository.RecordSource
import com.yugyd.quiz.domain.api.repository.ThemeSource
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.roundToInt

class ProgressInteractorImpl @Inject constructor(
    private val themeSource: ThemeSource,
    private val recordSource: RecordSource,
    private val dispatcherProvider: DispatchersProvider,
) : ProgressInteractor {

    override suspend fun getSpecificProgressItems(theme: Int) = withContext(dispatcherProvider.io) {
        val themeModel = themeSource.getTheme(theme)
        val progressItems = getModeProgressItems(themeModel)
        val totalProgress = getTotalProgress(progressItems.map(ModeProgress::percent))
        listOf(totalProgress).plus(progressItems)
    }

    override suspend fun isRecord(items: List<Any>) = withContext(dispatcherProvider.io) {
        items.filterIsInstance<ModeProgress>().any { it.record > 0 }
    }

    override suspend fun getProgressItems() = withContext(dispatcherProvider.io) {
        val themes = themeSource.getThemes()
        val records = recordSource.getRecords()
        val progressItems = aggregateThemesAndRecords(themes, records)
        val totalProgress = getTotalProgress(progressItems.map(Progress::percent))
        listOf(totalProgress).plus(progressItems)
    }

    private suspend fun getModeProgressItems(theme: Theme): List<ModeProgress> {
        val records = recordSource.getRecordsByTheme(theme.id)
        return recordModes.map { mode ->
            val record = records
                .firstOrNull { it.mode == mode }
                ?.record
                ?: 0

            val percent = percent(record, theme.count)

            ModeProgress(
                id = mode.ordinal,
                mode = mode,
                record = record,
                count = theme.count,
                percent = percent
            )
        }
    }

    private fun aggregateThemesAndRecords(themes: List<Theme>, records: List<Record>) = themes
        .map { theme ->
            val totalRecord = records
                .filter { it.themeId == theme.id }
                .sumOf { it.record }
            val percent = percent(totalRecord, theme.count, recordModeCount)
            val degree = degreeByValue(percent) ?: Degree.SCHOOLBOY

            Progress(
                id = theme.id,
                title = theme.name,
                percent = percent,
                degree = degree
            )
        }

    private fun getTotalProgress(progressPercents: List<Int>) = progressPercents
        .average()
        .roundToInt()
        .let { percent ->
            TotalProgress(
                percent = percent,
                degree = degreeByValue(percent) ?: Degree.SCHOOLBOY
            )
        }

    private fun degreeByValue(value: Int) = Degree
        .values()
        .sortedBy { it.value }
        .firstOrNull { it.value < value }
}
