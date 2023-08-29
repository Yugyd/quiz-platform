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

package com.yugyd.quiz

import android.content.Context
import com.yugyd.quiz.core.ContentProvider
import com.yugyd.quiz.core.GlobalConfig
import com.yugyd.quiz.core.ResIdProvider
import com.yugyd.quiz.featuretoggle.domain.RemoteConfigRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remoteConfigRepository: RemoteConfigRepository,
    private val resIdProvider: ResIdProvider,
) : ContentProvider {

    override suspend fun getTelegramChannel(): String {
        val config = remoteConfigRepository.fetchTelegramConfig()
        return config
            ?.links
            ?.firstOrNull {
                GlobalConfig.APPLICATION_ID.contains(it.packageX)
            }
            ?.link ?: context.getString(resIdProvider.appTelegramChat())
    }
}
