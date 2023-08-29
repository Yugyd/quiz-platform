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

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.yugyd.quiz.core.Logger
import timber.log.Timber

class LoggerImpl(private val context: Context) : Logger {

    override fun print(tag: String, message: String) {
        Timber.tag(tag).d(message)
    }

    override fun print(message: String) {
        Timber.tag(DEFAULT_TAG).d(message)
    }

    override fun logError(error: Throwable) {
        Timber.e(error)
    }

    override fun recordError(error: Throwable) {
        Timber.e(error)

        if (FirebaseApp.getApps(context).isNotEmpty()) {
            FirebaseCrashlytics.getInstance().recordException(error)
        }
    }

    companion object {
        const val DEFAULT_TAG = "QUIZ"
    }
}