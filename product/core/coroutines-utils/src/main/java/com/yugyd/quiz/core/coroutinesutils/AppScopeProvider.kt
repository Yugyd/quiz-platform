package com.yugyd.quiz.core.coroutinesutils

import kotlinx.coroutines.CoroutineScope

interface AppScopeProvider {
    val ioScope: CoroutineScope
    val defaultScope: CoroutineScope
    val mainScope: CoroutineScope
}
