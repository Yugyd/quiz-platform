package com.yugyd.quiz.core

interface AdIdJvmProvider {
    fun gameBannerAdId(): TextModel
    fun gameRewardedAdId(): TextModel
    fun themeRewardedAdId(): TextModel
    fun gameEndInterstitialAdId(): TextModel
}
