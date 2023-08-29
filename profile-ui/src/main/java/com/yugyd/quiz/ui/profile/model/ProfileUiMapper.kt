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

package com.yugyd.quiz.ui.profile.model

import android.content.Context
import androidx.annotation.StringRes
import com.yugyd.quiz.commonui.model.content.ContentUiMapper
import com.yugyd.quiz.core.ResIdProvider
import com.yugyd.quiz.domain.api.model.Content
import com.yugyd.quiz.domain.options.model.Transition
import com.yugyd.quiz.featuretoggle.domain.model.telegram.TelegramConfig
import com.yugyd.quiz.ui.profile.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ProfileUiMapper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val contentUiMapper: ContentUiMapper,
    private val resIdProvider: ResIdProvider
) {

    private val appName by lazy(LazyThreadSafetyMode.NONE) {
        context.getString(resIdProvider.appName())
    }

    fun map(
        content: Content,
        transition: Transition,
        isSortedQuest: Boolean,
        isVibration: Boolean,
        isProFeatureEnabled: Boolean,
        isTelegramFeatureEnabled: Boolean,
        telegramConfig: TelegramConfig?
    ) = listOfNotNull(
        header(content, isProFeatureEnabled),
        section(TypeProfile.SOCIAL_SECTION, R.string.title_social, isTelegramFeatureEnabled),
        social(TypeProfile.TELEGRAM_SOCIAL, isTelegramFeatureEnabled, telegramConfig),
        section(TypeProfile.SETTINGS_SECTION, R.string.title_settings),
        value(TypeProfile.TRANSITION, R.string.title_show_answer, transition.value),
        switch(TypeProfile.SORT_QUEST, R.string.title_sorting_quest, isSortedQuest),
        switch(TypeProfile.VIBRATION, R.string.title_vibration, isVibration),
        getProItem(isProFeatureEnabled),
        getPurchaseSection(isProFeatureEnabled),
        section(TypeProfile.PLEASE_US_SECTION, R.string.title_please_use),
        item(TypeProfile.RATE_APP, R.string.title_rate_app),
        item(TypeProfile.SHARE, R.string.title_share_friend),
        item(TypeProfile.OTHER_APPS, R.string.title_other_apps),
        section(TypeProfile.FEEDBACK_SECTION, R.string.title_feedback),
        item(TypeProfile.REPORT_ERROR, R.string.title_report_error),
        item(TypeProfile.PRIVACY_POLICY, R.string.title_privacy_policy)
    )

    private fun header(content: Content, isProFeatureEnabled: Boolean) = content.run {
        val contentUi = contentUiMapper.map(content)
        val contentTitle = context.getString(contentUi.title)
        val formattedVersionName = context.getString(R.string.format_version, contentTitle)

        HeaderProfileUiModel(
            appName = appName,
            appIcon = resIdProvider.appIcon(),
            versionTitle = formattedVersionName,
            isVersionTitleVisible = isProFeatureEnabled
        )
    }

    private fun section(
        type: TypeProfile,
        @StringRes titleRes: Int,
        isSectionEnabled: Boolean = true,
    ): SectionProfileUiModel? {
        return if (isSectionEnabled) {
            SectionProfileUiModel(
                id = type.id,
                title = context.getString(titleRes)
            )
        } else {
            null
        }
    }

    private fun value(type: TypeProfile, @StringRes titleRes: Int, value: Double) =
        ValueItemProfileUiModel(
            id = type.id,
            type = type,
            title = context.getString(titleRes),
            value = context.getString(R.string.format_time_second, value)
        )

    private fun switch(type: TypeProfile, @StringRes titleRes: Int, isChecked: Boolean) =
        SwitchItemProfileUiModel(
            id = type.id,
            type = type,
            title = context.getString(titleRes),
            isChecked = isChecked
        )

    private fun item(type: TypeProfile, @StringRes titleRes: Int) = SelectItemProfileUiModel(
        id = type.id,
        type = type,
        title = context.getString(titleRes)
    )

    private fun social(
        type: TypeProfile,
        isTelegramFeatureEnabled: Boolean,
        telegramConfig: TelegramConfig?
    ): SocialItemProfileUiModel? {
        return if (isTelegramFeatureEnabled && telegramConfig != null) {
            val telegramTitle = telegramConfig.profileCell.title.ifBlank {
                context.getString(R.string.profile_title_telegram)
            }
            val telegramMsg = telegramConfig.profileCell.message.ifBlank {
                context.getString(R.string.profile_title_telegram_promo)
            }
            SocialItemProfileUiModel(
                id = type.id,
                type = type,
                title = telegramTitle,
                message = telegramMsg,
                icon = R.drawable.ic_telegram,
            )
        } else {
            null
        }
    }

    private fun getPurchaseSection(isProFeatureEnabled: Boolean): SelectItemProfileUiModel? {
        return if (isProFeatureEnabled) {
            item(TypeProfile.PRO, R.string.title_pro_version)
        } else {
            null
        }
    }

    private fun getProItem(isProFeatureEnabled: Boolean): SectionProfileUiModel? {
        return if (isProFeatureEnabled) {
            section(TypeProfile.PURCHASES_SECTION, R.string.title_purchases)
        } else {
            null
        }
    }
}
