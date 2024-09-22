package com.yugyd.quiz.domain.tasks

import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import com.yugyd.quiz.remoteconfig.api.RemoteConfig
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class QueryFormatRepositoryImpl @Inject constructor(
    private val remoteConfig: RemoteConfig,
    private val dispatcherProvider: DispatchersProvider,
) : QueryFormatRepository {

    override suspend fun getFormatFromRemoteConfig() = withContext(dispatcherProvider.io) {
        remoteConfig.fetchStringValue(QUEST_QUERY_FORMAT_KEY)
    }

    companion object {
        private const val QUEST_QUERY_FORMAT_KEY = "quest_query_format"
    }
}
