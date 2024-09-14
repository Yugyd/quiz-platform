package com.yugyd.quiz.ad.rewarded

import android.app.Activity
import android.content.Context
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoadListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoader
import com.yugyd.quiz.ad.api.AdErrorDomainModel
import com.yugyd.quiz.ad.common.AdEventCallback
import com.yugyd.quiz.core.Logger

class YandexRewardedAdFactory(
    private val logger: Logger,
) : RewardedAdFactory {

    private var rewardedAd: RewardedAd? = null
    private var rewardedAdLoader: RewardedAdLoader? = null

    override fun create(
        context: Context,
        callback: AdEventCallback,
        isActivityDestroyProvider: () -> Boolean,
    ) {
        logger.print(TAG, "Create ad")

        rewardedAdLoader = RewardedAdLoader(context).apply {
            setAdLoadListener(
                object : RewardedAdLoadListener {
                    override fun onAdLoaded(rewarded: RewardedAd) {
                        if (isActivityDestroyProvider()) {
                            destroy()
                        } else {
                            rewardedAd = rewarded

                            logger.print(TAG, "On ad loaded")

                            callback.onAdLoaded()
                        }
                    }

                    override fun onAdFailedToLoad(error: AdRequestError) {
                        logger.print(TAG, "On ad failed to load")

                        callback.onAdFailedToLoad(
                            AdErrorDomainModel(
                                code = error.code,
                                message = error.description,
                            )
                        )
                    }
                }
            )
        }
    }

    override fun loadAd(adUnitId: String) {
        logger.print(TAG, "Load ad: $adUnitId")

        val adRequestConfiguration = AdRequestConfiguration
            .Builder(adUnitId)
            .build()
        rewardedAdLoader?.loadAd(adRequestConfiguration)
    }

    override fun showAd(activity: Activity, callback: AdRewardEventCallback) {
        logger.print(TAG, "Show ad")

        rewardedAd?.apply {
            setAdEventListener(
                object : RewardedAdEventListener {
                    override fun onAdShown() {
                        logger.print(TAG, "On ad shown")
                    }

                    override fun onAdFailedToShow(adError: AdError) {
                        logger.print(TAG, "On ad failed to show")

                        // Clean resources after Ad failed to show
                        cleanAd()

                        callback.onAdFailedToShow(
                            AdErrorDomainModel(
                                code = DEFAULT_CODE_ERROR,
                                message = adError.description,
                            )
                        )
                    }

                    override fun onAdDismissed() {
                        logger.print(TAG, "On ad dismissed")

                        // Called when ad is dismissed.
                        // Clean resources after Ad dismissed
                        cleanAd()

                        callback.onAdDismissed()
                    }

                    override fun onAdClicked() {
                        logger.print(TAG, "On ad clicked")
                    }

                    override fun onAdImpression(impressionData: ImpressionData?) {
                        logger.print(TAG, "On ad impression")
                    }

                    override fun onRewarded(reward: Reward) {
                        logger.print(TAG, "On rewarded: ${reward.amount}, ${reward.type}")

                        callback.onRewarded(
                            RewardDomainModel(
                                amount = reward.amount,
                                type = reward.type,
                            )
                        )
                    }
                }
            )

            show(activity)
        }
    }

    override fun destroy() {
        logger.print(TAG, "Destroy ad")

        clearLoader()
        cleanAd()
    }

    private fun clearLoader() {
        // Destroy loader
        rewardedAdLoader?.setAdLoadListener(null)
        rewardedAdLoader = null
    }

    private fun cleanAd() {
        // Destroy ad
        rewardedAd?.setAdEventListener(null)
        rewardedAd = null
    }

    companion object {
        private const val TAG = "YandexRewardedAdFactory"
        private const val DEFAULT_CODE_ERROR = -1
    }
}
