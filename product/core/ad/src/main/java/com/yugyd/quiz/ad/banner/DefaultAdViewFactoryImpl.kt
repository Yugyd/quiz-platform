package com.yugyd.quiz.ad.banner

import android.content.Context
import android.content.res.Resources
import android.view.View
import com.yugyd.quiz.ad.common.AdEventCallback

class DefaultAdViewFactoryImpl : AdViewFactory {
    override fun createAndLoadAd(
        context: Context,
        bannerAdUnitId: String,
        adContainerWidth: Int,
        resources: Resources,
        callback: AdEventCallback,
        isActivityDestroyProvider: () -> Boolean
    ): View {
        throw IllegalStateException("Ad not created in default factory")
    }

    override fun destroy(context: Context, adView: View?) {
        throw IllegalStateException("Ad not destroyed in default factory")
    }
}
