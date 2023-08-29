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

import android.app.Application
import com.yugyd.quiz.core.GlobalConfig
import com.yugyd.quiz.core.Logger
import com.yugyd.quiz.push.PushManager
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject

@HiltAndroidApp
class QuizApplication : Application() {

    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var pushManager: PushManager

    init {
        with(GlobalConfig) {
            DEBUG = BuildConfig.DEBUG
            APPLICATION_ID = BuildConfig.APPLICATION_ID
            PRO_APP_PACKAGE = BuildConfig.PRO_APP_PACKAGE
            DEV_ID = BuildConfig.DEV_ID
            PRIVACY_POLICY_LINK = BuildConfig.PRIVACY_POLICY_LINK
            VERSION_CODE = BuildConfig.VERSION_CODE
        }
    }

    override fun onCreate() {
        super.onCreate()

        if (GlobalConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        initLogger(this)

        pushManager.createChannels()
    }

    companion object {
        private lateinit var externalLogger: Logger

        private fun initLogger(application: QuizApplication) {
            externalLogger = application.logger
        }
    }
}
