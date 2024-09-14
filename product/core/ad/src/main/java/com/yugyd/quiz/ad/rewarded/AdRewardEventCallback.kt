package com.yugyd.quiz.ad.rewarded

import com.yugyd.quiz.ad.api.AdErrorDomainModel

interface AdRewardEventCallback {
    fun onRewarded(reward: RewardDomainModel)
    fun onAdDismissed()
    fun onAdFailedToShow(adError: AdErrorDomainModel)
}
