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

package com.yugyd.quiz.commonui.base

import androidx.lifecycle.ViewModel
import com.yugyd.quiz.core.Logger
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<State : Any, Action : Any> protected constructor(
    private val logger: Logger,
    initialState: State
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state

    protected var screenState: State
        get() = _state.value
        set(value) {
            _state.value = value
        }

    fun onAction(action: Action) {
        // TODO Add only debug
        logger.print(action.toString())

        handleAction(action)
    }

    protected abstract fun handleAction(action: Action)

    protected fun processError(error: Throwable) {
        if (error is CancellationException) {
            return
        }

        logger.recordError(error)
    }

    protected fun log(msg: String) {
        if (msg.isEmpty()) return
        logger.print(msg)
    }
}
