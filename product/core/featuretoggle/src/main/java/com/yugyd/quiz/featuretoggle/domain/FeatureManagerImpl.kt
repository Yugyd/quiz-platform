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

package com.yugyd.quiz.featuretoggle.domain

import com.yugyd.quiz.core.GlobalConfig
import com.yugyd.quiz.featuretoggle.domain.model.FeatureToggle
import timber.log.Timber
import javax.inject.Inject

internal class FeatureManagerImpl @Inject internal constructor(
    private val remoteConfigRepository: RemoteConfigRepository,
) : FeatureManager {

    override suspend fun isFeatureEnabled(featureToggle: FeatureToggle): Boolean {
        return when {
            GlobalConfig.DEBUG && featureToggle.isLocal -> featureToggle.localValue

            !GlobalConfig.DEBUG && featureToggle.isLocal -> false

            else -> {
                val result = runCatching {
                    remoteConfigRepository.fetchFeatureToggle(featureToggle)
                }
                result.getOrElse {
                    Timber.w(it)
                    FEATURE_DISABLED
                }
            }
        }
    }

    companion object {
        private const val FEATURE_DISABLED = false
    }
}
