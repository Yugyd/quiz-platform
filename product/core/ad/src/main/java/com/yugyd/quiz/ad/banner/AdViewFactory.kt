package com.yugyd.quiz.ad.banner

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.compose.runtime.compositionLocalOf
import com.yugyd.quiz.ad.common.AdEventCallback

val LocalAdViewFactory = compositionLocalOf<AdViewFactory> {
    DefaultAdViewFactoryImpl()
}

interface AdViewFactory {

    fun createAndLoadAd(
        context: Context,
        bannerAdUnitId: String,
        adContainerWidth: Int,
        resources: Resources,
        callback: AdEventCallback,
        isActivityDestroyProvider: () -> Boolean,
    ): View

    fun destroy(context: Context, adView: View?)
}
