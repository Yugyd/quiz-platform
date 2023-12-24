package com.yugyd.quiz.commonui.test

import com.yugyd.quiz.commonui.base.StateViewModel

val <T : Any> StateViewModel<T>.currentState: T
    get() = state.value
