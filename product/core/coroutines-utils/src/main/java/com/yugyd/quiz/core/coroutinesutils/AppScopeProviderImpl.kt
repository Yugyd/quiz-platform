package com.yugyd.quiz.core.coroutinesutils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class AppScopeProviderImpl(
    dispatchersProvider: DispatchersProvider,
) : AppScopeProvider {
    override val ioScope = CoroutineScope(SupervisorJob() + dispatchersProvider.io)
    override val defaultScope = CoroutineScope(SupervisorJob() + dispatchersProvider.default)
    override val mainScope = CoroutineScope(SupervisorJob() + dispatchersProvider.main)
}
