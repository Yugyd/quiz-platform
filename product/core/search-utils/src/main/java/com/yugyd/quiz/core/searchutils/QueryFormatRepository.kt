package com.yugyd.quiz.core.searchutils

interface QueryFormatRepository {
    suspend fun getFormatFromRemoteConfig(): String
}
