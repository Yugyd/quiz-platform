package com.yugyd.quiz.domain.content

interface ContentRemoteConfigSource {
    suspend fun getContentFormatUrl(): String
}
