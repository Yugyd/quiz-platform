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

package com.yugyd.quiz.proui.proonboarding

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.yugyd.quiz.proui.R
import com.yugyd.quiz.proui.proonboarding.model.ProOnboardingUiModel

internal class ProOnboardingPreviewParameterProvider :
    PreviewParameterProvider<List<ProOnboardingUiModel>> {

    companion object {
        private const val TEST_ITEM_COUNT = 2
    }

    override val values: Sequence<List<ProOnboardingUiModel>>
        get() = sequenceOf(
            buildList {
                repeat(TEST_ITEM_COUNT) {
                    add(
                        ProOnboardingUiModel(
                            id = it,
                            icon = R.drawable.ic_rocket_launch_48,
                            title = "Title $it",
                            subtitle = "Subtitle $it",
                            showBackground = true,
                        )
                    )
                }
            },
        )
}
