/*
 *    Copyright 2023 Roman Likhachev
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.yugyd.quiz.di

import com.yugyd.quiz.ad.api.AdClient
import com.yugyd.quiz.ad.banner.AdViewFactory
import com.yugyd.quiz.ad.banner.GoogleAdViewFactoryImpl
import com.yugyd.quiz.ad.banner.YandexAdViewFactoryImpl
import com.yugyd.quiz.ad.impl.YandexAdClientImpl
import com.yugyd.quiz.ad.interstitial.InterstitialAdFactory
import com.yugyd.quiz.ad.interstitial.YandexInterstitialAdFactory
import com.yugyd.quiz.ad.rewarded.RewardedAdFactory
import com.yugyd.quiz.ad.rewarded.YandexRewardedAdFactory
import com.yugyd.quiz.core.AdProviderType
import com.yugyd.quiz.core.GlobalConfig
import com.yugyd.quiz.core.Logger
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AdModule {

    @Binds
    internal abstract fun bindAdIdProvider(impl: YandexAdClientImpl): AdClient

    companion object {

        @Provides
        internal fun providesAdViewFactory(logger: Logger): AdViewFactory {
            return when (GlobalConfig.AD_PROVIDER) {
                AdProviderType.YANDEX -> YandexAdViewFactoryImpl(logger)
                AdProviderType.GOOGLE -> GoogleAdViewFactoryImpl()
            }
        }

        @Provides
        internal fun providesRewardedAdFactory(logger: Logger): RewardedAdFactory {
            return when (GlobalConfig.AD_PROVIDER) {
                AdProviderType.YANDEX -> YandexRewardedAdFactory(logger)
                AdProviderType.GOOGLE -> throw IllegalStateException("Not implemented")
            }
        }

        @Provides
        internal fun providesInterstitialAdFactory(logger: Logger): InterstitialAdFactory {
            return when (GlobalConfig.AD_PROVIDER) {
                AdProviderType.YANDEX -> YandexInterstitialAdFactory(logger)
                AdProviderType.GOOGLE -> throw IllegalStateException("Not implemented")
            }
        }
    }
}
