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
import com.yugyd.quiz.domain.content.api.ContentModel
import com.yugyd.quiz.domain.content.exceptions.ContentNotValidException

interface ContentInteractor {

    suspend fun isSelected(): Boolean

    suspend fun getSelectedContent(): ContentModel?

    suspend fun getContents(): List<ContentModel>

    suspend fun deleteContent(id: String)

    suspend fun addContent(
        oldModel: ContentModel,
        contentName: String,
        uri: Uri,
    ): Boolean

    @Throws(ContentNotValidException::class)
    suspend fun selectContent(oldModel: ContentModel, newModel: ContentModel): Boolean
}