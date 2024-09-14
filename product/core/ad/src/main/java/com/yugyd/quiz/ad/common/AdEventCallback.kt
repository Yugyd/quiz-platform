package com.yugyd.quiz.ad.common

import com.yugyd.quiz.ad.api.AdErrorDomainModel

interface AdEventCallback {
    fun onAdLoaded()
    fun onAdFailedToLoad(adError: AdErrorDomainModel)
}
