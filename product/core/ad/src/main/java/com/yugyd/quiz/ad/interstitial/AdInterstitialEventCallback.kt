package com.yugyd.quiz.ad.interstitial

import com.yugyd.quiz.ad.api.AdErrorDomainModel

interface AdInterstitialEventCallback {
    fun onAdDismissed()
    fun onAdFailedToShow(adError: AdErrorDomainModel)
}
