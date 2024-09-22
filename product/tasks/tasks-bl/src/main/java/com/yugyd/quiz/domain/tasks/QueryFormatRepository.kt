package com.yugyd.quiz.domain.tasks

internal interface QueryFormatRepository {
    suspend fun getFormatFromRemoteConfig(): String
}
