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

package com.yugyd.quiz.domain.options

import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import com.yugyd.quiz.domain.api.repository.PreferencesSource
import com.yugyd.quiz.domain.options.model.Transition
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

internal class OptionsInteractorImpl @Inject constructor(
    private val preferencesSource: PreferencesSource,
    private val dispatcherProvider: DispatchersProvider,
) : OptionsInteractor {

    override val transition: Transition
        get() = getTransitionByValue(preferencesSource.transition)

    override var isSortingQuest: Boolean
        get() = preferencesSource.isSortingQuest
        set(value) {
            preferencesSource.isSortingQuest = value
        }

    override var isVibration: Boolean
        get() = preferencesSource.isVibration
        set(value) {
            preferencesSource.isVibration = value
        }

    override val isNewVersionOnboardingShowed: Boolean
        get() {
            return if (preferencesSource.isNewVersionOnboardingShowed) {
                preferencesSource.isNewVersionOnboardingShowed = false
                true
            } else {
                false
            }
        }

    override var isRate: Boolean
        get() = preferencesSource.isRate
        set(value) {
            preferencesSource.isRate = value
        }

    override var isTelegramSubscription: Boolean
        get() = preferencesSource.isTelegramSubscription
        set(value) {
            preferencesSource.isTelegramSubscription = value
        }

    override fun isTelegramPopupShow(): Boolean {
        synchronized(this) {
            val nextTelegramDateTime = preferencesSource.nextTelegramDate.time
            val currentDateTime = Date().time
            return currentDateTime >= nextTelegramDateTime
        }
    }

    override fun setNextPopupTelegramDate() {
        synchronized(this) {
            val currentDate = Date()
            val calendar = Calendar.getInstance()
            calendar.time = currentDate
            calendar.add(Calendar.DATE, TELEGRAM_POPUP_PERIOD)

            val newDate = calendar.time
            preferencesSource.nextTelegramDate = newDate
        }
    }

    private fun getTransitionByValue(value: Double) = Transition.values().firstOrNull {
        it.value == value
    } ?: Transition.TRANSITION2000

    override fun getTransitions() = Transition.values().toList()

    override suspend fun setTransition(value: Double) = withContext(dispatcherProvider.io) {
        preferencesSource.transition = getTransitionByValue(value).value
    }

    companion object {
        private const val TELEGRAM_POPUP_PERIOD = 7
    }
}