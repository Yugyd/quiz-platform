package com.yugyd.quiz.core.coroutinesutils

import kotlinx.coroutines.CoroutineDispatcher

class DispatchersProviderImpl(
    override val io: CoroutineDispatcher,
    override val default: CoroutineDispatcher,
    override val main: CoroutineDispatcher
) : DispatchersProvider
