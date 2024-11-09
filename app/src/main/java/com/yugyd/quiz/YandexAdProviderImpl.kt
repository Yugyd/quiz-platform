package com.yugyd.quiz

import com.yugyd.quiz.core.AdIdProvider
import com.yugyd.quiz.core.GlobalConfig
import com.yugyd.quiz.core.TextModel
import javax.inject.Inject

internal class YandexAdProviderImpl @Inject constructor() : AdIdProvider {

    override fun idAdBannerGame(): Int {
        return if (GlobalConfig.DEBUG) {
            R.string.test_id_yandex_ad_banner_game
        } else {
            R.string.id_yandex_ad_banner_game
        }
    }

    override fun idAdRewardedGame(): Int {
        return if (GlobalConfig.DEBUG) {
            R.string.test_id_yandex_ad_rewarded_game
        } else {
            R.string.id_yandex_ad_rewarded_game
        }
    }

    override fun idAdInterstitialGameEnd(): Int {
        return if (GlobalConfig.DEBUG) {
            R.string.test_id_yandex_ad_interstitial_game_end
        } else {
            R.string.id_yandex_ad_interstitial_game_end
        }
    }

    override fun idAdRewardedTheme(): Int {
        return if (GlobalConfig.DEBUG) {
            R.string.test_id_yandex_ad_rewarded_theme
        } else {
            R.string.id_yandex_ad_rewarded_theme
        }
    }

    override fun gameBannerAdId() = TextModel.ResTextModel(res = idAdBannerGame())
    override fun gameRewardedAdId() = TextModel.ResTextModel(res = idAdRewardedGame())
    override fun themeRewardedAdId() = TextModel.ResTextModel(res = idAdRewardedTheme())
    override fun gameEndInterstitialAdId() = TextModel.ResTextModel(res = idAdInterstitialGameEnd())
}
