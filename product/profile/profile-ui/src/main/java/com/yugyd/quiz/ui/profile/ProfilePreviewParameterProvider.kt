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

package com.yugyd.quiz.ui.profile

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.yugyd.quiz.ui.profile.model.HeaderProfileUiModel
import com.yugyd.quiz.ui.profile.model.OpenSourceProfileUiModel
import com.yugyd.quiz.ui.profile.model.ProfileUiModel
import com.yugyd.quiz.ui.profile.model.SectionProfileUiModel
import com.yugyd.quiz.ui.profile.model.SelectItemProfileUiModel
import com.yugyd.quiz.ui.profile.model.SocialItemProfileUiModel
import com.yugyd.quiz.ui.profile.model.SwitchItemProfileUiModel
import com.yugyd.quiz.ui.profile.model.TypeProfile
import com.yugyd.quiz.ui.profile.model.ValueItemProfileUiModel
import com.yugyd.quiz.uikit.R as UiKitR

internal class ProfilePreviewParameterProvider : PreviewParameterProvider<List<ProfileUiModel>> {

    override val values: Sequence<List<ProfileUiModel>>
        get() = sequenceOf(
            buildList {
                add(
                    HeaderProfileUiModel(
                        id = -1,
                        appName = "App name",
                        appIcon = UiKitR.drawable.ic_account_circle,
                        versionTitle = "Version title",
                        isVersionTitleVisible = true
                    ),
                )
                add(
                    SectionProfileUiModel(
                        id = TypeProfile.FEEDBACK_SECTION.id,
                        title = "Section title",
                    )
                )
                add(
                    SelectItemProfileUiModel(
                        id = TypeProfile.RATE_APP.id,
                        type = TypeProfile.RATE_APP,
                        title = "Select item",
                    )
                )
                add(
                    ValueItemProfileUiModel(
                        id = TypeProfile.TRANSITION.id,
                        type = TypeProfile.TRANSITION,
                        title = "Value item",
                        value = "Value",
                    )
                )
                add(
                    SwitchItemProfileUiModel(
                        id = TypeProfile.VIBRATION.id,
                        type = TypeProfile.VIBRATION,
                        title = "Switch item",
                        isChecked = true
                    )
                )
                add(
                    SocialItemProfileUiModel(
                        id = TypeProfile.TELEGRAM_SOCIAL.id,
                        type = TypeProfile.TELEGRAM_SOCIAL,
                        title = "Telegram title",
                        message = "Telegram message",
                        icon = R.drawable.ic_telegram,
                    )
                )
                add(
                    OpenSourceProfileUiModel()
                )
            },
        )
}