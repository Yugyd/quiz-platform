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

import android.net.Uri
import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import com.yugyd.quiz.domain.content.api.ContentModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ContentInteractorImpl @Inject constructor(
    private val contentClient: ContentClient,
    private val contentPreferencesSource: ContentPreferencesSource,
    private val dispatchersProvider: DispatchersProvider,
) : ContentInteractor {

    override suspend fun isSelected() = withContext(dispatchersProvider.io) {
        !contentPreferencesSource.databaseMarker.isNullOrEmpty() && contentClient.isSelected()
    }

    override suspend fun getSelectedContent() = withContext(dispatchersProvider.io) {
        contentClient.getSelectedContent()
    }

    override suspend fun getContents() = withContext(dispatchersProvider.io) {
        contentClient.getContents()
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
        oldModel: ContentModel,
        contentName: String,
        uri: Uri,
    ) = withContext(dispatchersProvider.io) {
        contentClient.setContent(
            oldModel = oldModel,
            contentName = contentName,
            uri = uri,
        )
    }
}
