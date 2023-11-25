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

package com.yugyd.quiz.domain.controller

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransitionController @Inject constructor() {
    private val listeners = arrayListOf<Listener>()

    fun subscribe(listener: Listener) = listeners.add(listener)

    fun unsubscribe(listener: Listener) = listeners.remove(listener)

    fun notifyEvent() = listeners.forEach { it.onTransitionChanged() }

    interface Listener {
        fun onTransitionChanged()
    }
}