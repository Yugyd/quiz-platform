package com.yugyd.quiz.commonui.base

import kotlinx.coroutines.flow.StateFlow

interface StateViewModel<out T : Any> {
    val state: StateFlow<T>
}
