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

package com.yugyd.quiz.themeui.theme.model

import android.content.Context
import com.yugyd.quiz.featuretoggle.domain.model.telegram.TelegramConfig
import com.yugyd.quiz.themeui.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RewardDialogUiMapper @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun mapToTelegram(
        config: TelegramConfig,
        model: ThemeUiModel
    ) = config.trainPopup.run {
        RewardDialogUiModel(
            title = title,
            message = message,
            positiveButton = positiveButtonTitle,
            negativeButton = negativeButtonTitle,
            rewardType = RewardType.Telegram,
            themeUiModel = model
        )
    }

    fun mapToAd(
        model: ThemeUiModel
    ): RewardDialogUiModel {
        return RewardDialogUiModel(
            title = context.getString(R.string.title_rewarded),
            message = context.getString(R.string.msg_rewarded),
            positiveButton = context.getString(R.string.action_rewarded),
            negativeButton = context.getString(R.string.action_no_thanks),
            rewardType = RewardType.Ad,
            themeUiModel = model
        )
    }
}
