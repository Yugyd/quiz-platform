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

package com.yugyd.quiz.domain.theme

import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import com.yugyd.quiz.domain.api.model.Mode
import com.yugyd.quiz.domain.api.model.Record
import com.yugyd.quiz.domain.api.model.Theme
import com.yugyd.quiz.domain.api.repository.RecordSource
import com.yugyd.quiz.domain.api.repository.ThemeSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ThemeInteractorImpl(
    private val themeSource: ThemeSource,
    private val recordSource: RecordSource,
    private val dispatcherProvider: DispatchersProvider,
) : ThemeInteractor {

    override suspend fun getTheme(id: Int) = withContext(dispatcherProvider.io) {
        themeSource.getTheme(id)
    }

    override suspend fun getThemes(mode: Mode): List<Theme> = withContext(dispatcherProvider.io) {
        val themes = themeSource.getThemes()
        val records = recordSource.getRecordsByMode(mode)
        aggregateThemesAndRecords(themes, records)
    }

    private fun aggregateThemesAndRecords(themes: List<Theme>, records: List<Record>) =
        themes.map { theme ->
            theme.copy(
                progress = getRecord(records, theme.id)
            )
        }

    private fun getRecord(records: List<Record>, themeId: Int) = records.firstOrNull { record ->
        record.themeId == themeId
    }?.record ?: 0
}
