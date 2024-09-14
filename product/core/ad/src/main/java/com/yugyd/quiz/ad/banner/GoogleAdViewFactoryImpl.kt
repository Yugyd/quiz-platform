package com.yugyd.quiz.ad.banner

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.yugyd.quiz.ad.common.AdEventCallback

class GoogleAdViewFactoryImpl : AdViewFactory {

    @SuppressLint("MissingPermission")
    override fun createAndLoadAd(
        context: Context,
        bannerAdUnitId: String,
        adContainerWidth: Int,
        resources: Resources,
        callback: AdEventCallback,
        isActivityDestroyProvider: () -> Boolean
    ): View {
        return AdView(context).apply {
            setAdSize(AdSize.SMART_BANNER)
            adUnitId = bannerAdUnitId
            loadAd(AdRequest.Builder().build())
        }
    }

    override fun destroy(context: Context, adView: View?) {
        (adView as? AdView)?.destroy()
    }
}
