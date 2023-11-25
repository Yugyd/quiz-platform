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

package com.yugyd.quiz.domain.content.data

import com.yugyd.quiz.data.database.user.dao.ContentDao
import com.yugyd.quiz.data.model.mappers.ContentEntityMapper
import com.yugyd.quiz.domain.content.ContentSource
import com.yugyd.quiz.domain.content.api.ContentModel
import javax.inject.Inject

internal class ContentDataSource @Inject constructor(
    private val contentDao: ContentDao,
    private val entityMapper: ContentEntityMapper
) : ContentSource {

    override suspend fun getData(): List<ContentModel> {
        val contents = contentDao.getAll()
        return entityMapper.mapToDomain(contents)
    }

    override suspend fun getSelectedContent(): ContentModel? {
        val content = contentDao.getSelectedContent()
        return content?.let(entityMapper::mapToDomain)
    }

    override suspend fun deleteContent(id: String) {
        contentDao.delete(id.toInt())
    }

    override suspend fun addContent(contentModel: ContentModel) {
        val entity = entityMapper.mapToEntity(contentModel)
        contentDao.insert(entity)
    }

    override suspend fun updateContent(contentModel: ContentModel) {
        val entity = entityMapper.mapToEntity(contentModel)
        contentDao.update(entity)
    }
}
