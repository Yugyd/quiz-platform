package com.yugyd.quiz.ad.interstitial

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.compositionLocalOf
import com.yugyd.quiz.ad.common.AdEventCallback

val LocalInterstitialAdFactory = compositionLocalOf<InterstitialAdFactory> {
    DefaultInterstitialAdFactory()
}

interface InterstitialAdFactory {

    fun create(
        context: Context,
        callback: AdEventCallback,
        isActivityDestroyProvider: () -> Boolean,
    )

    fun loadAd(adUnitId: String)

    fun showAd(activity: Activity, callback: AdInterstitialEventCallback)

    fun destroy()
}
