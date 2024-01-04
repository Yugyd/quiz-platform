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

package com.yugyd.quiz.domain.content

import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import com.yugyd.quiz.core.file.FileRepository
import com.yugyd.quiz.domain.content.api.ContentModel
import com.yugyd.quiz.domain.content.models.ContentResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ContentInteractorImpl @Inject constructor(
    private val contentClient: ContentClient,
    private val fileRepository: FileRepository,
    private val dispatchersProvider: DispatchersProvider,
    private val contentRemoteConfigSource: ContentRemoteConfigSource,
) : ContentInteractor {

    override suspend fun getContentNameFromUri(uri: String) = withContext(dispatchersProvider.io) {
        fileRepository.getFileName(uri)?.substringBeforeLast(".")
    }

    override suspend fun isSelected() = withContext(dispatchersProvider.io) {
        contentClient.isSelected()
    }

    override suspend fun getSelectedContent() = withContext(dispatchersProvider.io) {
        contentClient.getSelectedContent()
    }

    override suspend fun getContents() = withContext(dispatchersProvider.io) {
        contentClient.getContents()
    }

    override fun subscribeToContents(): Flow<List<ContentModel>> {
        return contentClient
            .subscribeToContents()
            .flowOn(dispatchersProvider.io)
    }

    override fun subscribeToSelectedContent(): Flow<ContentModel?> {
        return contentClient
            .subscribeToSelectedContent()
            .flowOn(dispatchersProvider.io)
    }

    override fun isResetNavigation(
        oldModel: ContentModel?,
        newModel: ContentModel?,
    ): ContentResult {
        return ContentResult(
            isBack = oldModel != null && oldModel != newModel,
            newModel = newModel,
        )
    }

    override suspend fun deleteContent(id: String) = withContext(dispatchersProvider.io) {
        contentClient.deleteContent(id)
    }

    override suspend fun selectContent(
        oldModel: ContentModel,
        newModel: ContentModel,
    ) = withContext(dispatchersProvider.io) {
        contentClient.setContent(
            newModel = newModel,
            oldModel = oldModel,
        )
    }

    override suspend fun addContent(
        oldModel: ContentModel?,
        contentName: String?,
        uri: String,
    ) = withContext(dispatchersProvider.io) {
        contentClient.setContent(
            oldModel = oldModel,
            contentName = contentName,
            uri = uri,
        )
    }

    override suspend fun getContentFormatUrl() = withContext(dispatchersProvider.io) {
        contentRemoteConfigSource.getContentFormatUrl()
    }
}
