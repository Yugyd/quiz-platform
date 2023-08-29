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

package com.yugyd.quiz

import com.yugyd.quiz.core.AdIdProvider
import javax.inject.Inject

class AdIdProviderImpl @Inject constructor() : AdIdProvider {
    override fun idAdBannerGame() = R.string.id_ad_banner_game
    override fun idAdRewardedGame() = R.string.id_ad_rewarded_game
    override fun idAdInterstitialGameEnd() = R.string.id_ad_interstitial_game_end
    override fun idAdRewardedTheme() = R.string.id_ad_rewarded_theme
}
