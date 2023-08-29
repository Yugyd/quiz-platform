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

package com.yugyd.quiz.domain.update

import com.yugyd.quiz.core.GlobalConfig
import com.yugyd.quiz.featuretoggle.domain.RemoteConfigRepository
import timber.log.Timber

class UpdateInteractorImpl(
    private val remoteConfigRepository: RemoteConfigRepository
) : UpdateInteractor {

    override suspend fun shouldForceUpdateApp(): Boolean {
        val result = runCatching {
            remoteConfigRepository.fetchForceUpdateVersion()
        }

        return result.getOrElse {
            Timber.w(it)
            0
        } > GlobalConfig.VERSION_CODE
    }
}
