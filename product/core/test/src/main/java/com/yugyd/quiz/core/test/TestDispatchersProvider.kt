package com.yugyd.quiz.core.test

import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TestDispatchersProvider(
    override val io: CoroutineDispatcher = Dispatchers.Unconfined,
    override val default: CoroutineDispatcher = Dispatchers.Unconfined,
    override val main: CoroutineDispatcher = Dispatchers.Unconfined,
) : DispatchersProvider
