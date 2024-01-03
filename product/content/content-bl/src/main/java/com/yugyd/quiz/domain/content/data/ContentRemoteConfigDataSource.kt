package com.yugyd.quiz.domain.content.data

import com.yugyd.quiz.domain.content.ContentRemoteConfigSource
import com.yugyd.quiz.remoteconfig.api.RemoteConfig
import javax.inject.Inject

internal class ContentRemoteConfigDataSource @Inject constructor(
    private val remoteConfig: RemoteConfig,
) : ContentRemoteConfigSource {

    override suspend fun getContentFormatUrl(): String {
        return remoteConfig.fetchStringValue(CONTENT_FORMAT_URL_KEY)
    }

    companion object {
        private const val CONTENT_FORMAT_URL_KEY = "content_format_url"
    }
}
