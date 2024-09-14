package com.yugyd.quiz.ad.interstitial

import android.app.Activity
import android.content.Context
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader
import com.yugyd.quiz.ad.api.AdErrorDomainModel
import com.yugyd.quiz.ad.common.AdEventCallback
import com.yugyd.quiz.core.Logger

class YandexInterstitialAdFactory(
    private val logger: Logger,
) : InterstitialAdFactory {

    private var interstitialAd: InterstitialAd? = null
    private var interstitialAdLoader: InterstitialAdLoader? = null

    override fun create(
        context: Context,
        callback: AdEventCallback,
        isActivityDestroyProvider: () -> Boolean
    ) {
        logger.print(TAG, "Create ad")

        interstitialAdLoader = InterstitialAdLoader(context).apply {
            setAdLoadListener(
                object : InterstitialAdLoadListener {
                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        if (isActivityDestroyProvider()) {
                            destroy()
                        } else {
                            this@YandexInterstitialAdFactory.interstitialAd = interstitialAd

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

        val adRequestConfiguration = AdRequestConfiguration.Builder(adUnitId)
            .build()

        interstitialAdLoader?.loadAd(adRequestConfiguration)
    }

    override fun showAd(activity: Activity, callback: AdInterstitialEventCallback) {
        logger.print(TAG, "Show ad")

        interstitialAd?.apply {
            setAdEventListener(
                object : InterstitialAdEventListener {
                    override fun onAdShown() {
                        logger.print(TAG, "On ad shown")
                    }

                    override fun onAdFailedToShow(adError: AdError) {
                        logger.print(TAG, "On ad failed to show")

                        // Clean resources after Ad failed to show
                        cleanAd()

                        callback.onAdFailedToShow(
                            AdErrorDomainModel(
                                code = -1,
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
        interstitialAdLoader?.setAdLoadListener(null)
        interstitialAdLoader = null
    }

    private fun cleanAd() {
        // Destroy ad
        interstitialAd?.setAdEventListener(null)
        interstitialAd = null
    }

    companion object {
        private const val TAG = "YandexInterstitialAdFactory"
    }
}
