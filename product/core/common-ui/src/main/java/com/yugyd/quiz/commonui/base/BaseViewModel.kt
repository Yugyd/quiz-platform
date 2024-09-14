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
import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<State : Any, Action : Any> protected constructor(
    private val logger: Logger,
    dispatchersProvider: DispatchersProvider,
    initialState: State,
) : ViewModel(), StateViewModel<State> {

    private val tagNameWithClassName = this::class.simpleName.toString()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, error ->
        processError(error)
    }
    protected val vmScopeErrorHandled = CoroutineScope(
        SupervisorJob() + dispatchersProvider.main + coroutineExceptionHandler,
    )

    private val _state = MutableStateFlow(initialState)
    override val state: StateFlow<State> = _state.asStateFlow()

    protected var screenState: State
        get() = _state.value
        set(value) {
            logger.printIfDebug(tagNameWithClassName, "State: $value")
            _state.value = value
        }

    fun onAction(action: Action) {
        logger.printIfDebug(tagNameWithClassName, "Action: $action")

        handleAction(action)
    }

    protected abstract fun handleAction(action: Action)

    protected fun processError(error: Throwable) {
        if (error is CancellationException) {
            return
        }

        logger.recordError(error)
    }
}
