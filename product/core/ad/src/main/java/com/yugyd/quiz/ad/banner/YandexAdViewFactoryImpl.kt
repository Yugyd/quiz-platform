package com.yugyd.quiz.ad.banner

import android.content.Context
import android.content.res.Resources
import android.view.View
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yugyd.quiz.ad.api.AdErrorDomainModel
import com.yugyd.quiz.ad.common.AdEventCallback
import com.yugyd.quiz.core.Logger
import kotlin.math.roundToInt

class YandexAdViewFactoryImpl(private val logger: Logger) : AdViewFactory {

    override fun createAndLoadAd(
        context: Context,
        bannerAdUnitId: String,
        adContainerWidth: Int,
        resources: Resources,
        callback: AdEventCallback,
        isActivityDestroyProvider: () -> Boolean,
    ): View {
        val bannerAd = BannerAdView(context).apply {
            val adSize = getAdSize(
                context = context,
                adContainerWidth = adContainerWidth,
                resources = resources,
            )
            setAdSize(adSize)

            setAdUnitId(bannerAdUnitId)

            setBannerAdEventListener(
                this,
                callback,
                isActivityDestroyProvider,
            )

            loadAd(
                AdRequest.Builder().build()
            )
        }
        return bannerAd
    }

    private fun BannerAdView.setBannerAdEventListener(
        bannerAd: BannerAdView,
        callback: AdEventCallback,
        isDestroyProvider: () -> Boolean,
    ) {
        setBannerAdEventListener(
            object : BannerAdEventListener {
                override fun onAdLoaded() {
                    if (isDestroyProvider()) {
                        bannerAd.destroy()
                        return
                    }

                    logger.print(TAG, "On ad loaded")

                    callback.onAdLoaded()
                }

                override fun onAdFailedToLoad(error: AdRequestError) {
                    logger.print(TAG, "On ad failed to load: $error")

                    callback.onAdFailedToLoad(
                        AdErrorDomainModel(
                            code = error.code,
                            message = error.description,
                        )
                    )
                }

                override fun onAdClicked() {
                    logger.print(TAG, "On ad clicked")
                }

                override fun onLeftApplication() {
                    logger.print(TAG, "On letft application")
                }

                override fun onReturnedToApplication() {
                    logger.print(TAG, "On returned to application")
                }

                override fun onImpression(impressionData: ImpressionData?) {
                    logger.print(TAG, "Ad on impression")
                }
            }
        )
    }

    override fun destroy(
        context: Context,
        adView: View?,
    ) {
        (adView as? BannerAdView)?.destroy()
    }

    private fun getAdSize(
        context: Context,
        adContainerWidth: Int,
        resources: Resources,
    ): BannerAdSize {
        // Calculate the width of the ad, taking into account the padding in the ad container.
        var adWidthPixels = adContainerWidth
        if (adWidthPixels == 0) {
            // If the ad hasn't been laid out, default to the full screen width
            adWidthPixels = resources.displayMetrics.widthPixels
        }
        val adWidth = (adWidthPixels / resources.displayMetrics.density).roundToInt()

        return BannerAdSize.stickySize(context, adWidth)
    }

    companion object {
        private const val TAG = "YandexAdViewFactoryImpl"
    }
}
