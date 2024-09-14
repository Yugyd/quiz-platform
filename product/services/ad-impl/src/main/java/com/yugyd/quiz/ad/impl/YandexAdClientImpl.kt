package com.yugyd.quiz.ad.impl

import android.content.Context
import com.yandex.mobile.ads.common.MobileAds
import com.yugyd.quiz.ad.api.AdClient
import com.yugyd.quiz.core.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class YandexAdClientImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val logger: Logger,
) : AdClient {

    override fun initialize() {
        MobileAds.initialize(context) {
            logger.print(TAG, "Initialize mobile ad is completed")
        }
    }

    companion object {
        private const val TAG = "YandexAdClientImpl"
    }
}
