package com.yugyd.quiz.ad.rewarded

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.compositionLocalOf
import com.yugyd.quiz.ad.common.AdEventCallback

val LocalRewardAdFactory = compositionLocalOf<RewardedAdFactory> {
    DefaultRewardedAdFactory()
}

interface RewardedAdFactory {

    fun create(
        context: Context,
        callback: AdEventCallback,
        isActivityDestroyProvider: () -> Boolean,
    )

    fun loadAd(adUnitId: String)

    fun showAd(activity: Activity, callback: AdRewardEventCallback)

    fun destroy()
}

