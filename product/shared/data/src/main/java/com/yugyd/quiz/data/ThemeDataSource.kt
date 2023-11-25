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

import com.yugyd.quiz.data.database.content.dao.QuestDao
import com.yugyd.quiz.data.database.content.dao.ThemeDao
import com.yugyd.quiz.data.model.mappers.ThemeEntityMapper
import com.yugyd.quiz.domain.api.model.Theme
import com.yugyd.quiz.domain.api.repository.ThemeSource

class ThemeDataSource(
    private val questDao: QuestDao,
    private val themeDao: ThemeDao,
    private val themeEntityMapper: ThemeEntityMapper
) : ThemeSource {

    override suspend fun getSectionCount(themeId: Int) = questDao
        .getSectionCountByTheme(themeId)

    override suspend fun addThemes(themes: List<Theme>) {
        val entities = themes.map(themeEntityMapper::mapThemeToEntity)
        themeDao.insertAll(entities)
    }

    override suspend fun getTheme(id: Int) = themeDao
        .getThemeById(id)
        .let(themeEntityMapper::mapThemeToDomain)

    override suspend fun getThemes() = themeDao
        .getAll()
        .map(themeEntityMapper::mapThemeToDomain)
        .filterNot { it.id == DEFAULT_ALL_THEME }

    companion object {
        private const val DEFAULT_ALL_THEME = 0
    }
}