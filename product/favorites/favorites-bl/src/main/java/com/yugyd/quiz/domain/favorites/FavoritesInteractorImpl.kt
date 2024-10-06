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

package com.yugyd.quiz.domain.favorites

import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoritesInteractorImpl @Inject constructor(
    private val source: FavoritesSource,
    private val dispatcherProvider: DispatchersProvider,
) : FavoritesInteractor {

    override suspend fun updateTask(id: Int, isFavorite: Boolean) {
        if (isFavorite) {
            addTask(id)
        } else {
            deleteTask(id)
        }
    }

    override suspend fun getFavorites(): List<Int> = withContext(dispatcherProvider.io) {
        source.getTaskIds()
    }

    override suspend fun getFavorites(
        ids: List<Int>,
    ): List<Int> = withContext(dispatcherProvider.io) {
        source
            .getTaskIds()
            .filter(ids::contains)
    }

    private suspend fun addTask(id: Int) = withContext(dispatcherProvider.io) {
        source.addTask(id)
    }

    private suspend fun deleteTask(id: Int) = withContext(dispatcherProvider.io) {
        source.deleteTask(id)
    }
}
