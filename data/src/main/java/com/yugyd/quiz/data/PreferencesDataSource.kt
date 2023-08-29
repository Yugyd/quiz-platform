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
import androidx.core.content.edit
import com.yugyd.quiz.domain.repository.PreferencesSource
import java.util.Date

class PreferencesDataSource(context: Context) : PreferencesSource {

    private val preferences =
        context.getSharedPreferences(OPTIONS_PREFERENCES, Context.MODE_PRIVATE)

    override var transition: Double
        get() = preferences.getLong(PREF_TRANSITION, 0).let { Double.fromBits(it) }
        set(value) = preferences.edit { putLong(PREF_TRANSITION, value.toRawBits()) }

    override var isSortingQuest: Boolean
        get() = preferences.getBoolean(PREF_SORTING, true)
        set(value) = preferences.edit { putBoolean(PREF_SORTING, value) }

    override var isVibration: Boolean
        get() = preferences.getBoolean(PREF_VIBRATION, true)
        set(value) = preferences.edit { putBoolean(PREF_VIBRATION, value) }

    override var isNewVersionOnboardingShowed: Boolean
        get() = preferences.getBoolean(PREF_NEW_VERSION, true)
        set(value) = preferences.edit { putBoolean(PREF_NEW_VERSION, value) }

    override var isRate: Boolean
        get() = preferences.getBoolean(PREF_ASSESSMENT, true)
        set(value) = preferences.edit { putBoolean(PREF_ASSESSMENT, value) }

    override var isTelegramSubscription: Boolean
        get() = preferences.getBoolean(PREF_TELEGRAM_SUBSCRIPTION, false)
        set(value) = preferences.edit { putBoolean(PREF_TELEGRAM_SUBSCRIPTION, value) }

    override var nextTelegramDate: Date
        get() = preferences.getLong(PREF_NEXT_MAIN_POPUP_TELEGRAM_DATE, 0L).let {
            if (it == 0L) {
                Date()
            } else {
                Date(it)
            }
        }
        set(value) = preferences.edit {
            putLong(PREF_NEXT_MAIN_POPUP_TELEGRAM_DATE, value.time)
        }

    companion object {
        private const val OPTIONS_PREFERENCES = "options"
        private const val PREF_TRANSITION = "PREF_TRANSITION"
        private const val PREF_VIBRATION = "PREF_VIBRATION"
        private const val PREF_SORTING = "PREF_SORTING"
        private const val PREF_ASSESSMENT = "PREF_ASSESSMENT"
        private const val PREF_NEW_VERSION = "PREF_NEW_VERSION"
        private const val PREF_NEXT_MAIN_POPUP_TELEGRAM_DATE =
            "com.yugyd.quiz.PREF_NEXT_MAIN_POPUP_TELEGRAM_DATE"
        private const val PREF_TELEGRAM_SUBSCRIPTION = "com.yugyd.quiz.PREF_TELEGRAM_SUBSCRIPTION"
    }
}