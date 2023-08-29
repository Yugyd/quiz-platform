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

package com.yugyd.quiz.progressui.shared

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.yugyd.quiz.progressui.model.HeaderProgressUiModel
import com.yugyd.quiz.progressui.model.ItemProgressUiModel
import com.yugyd.quiz.progressui.model.ProgressUiModel
import com.yugyd.quiz.uikit.theme.app_color_neutral

internal class ProgressPreviewParameterProvider : PreviewParameterProvider<List<ProgressUiModel>> {

    companion object {
        private const val TEST_ITEM_COUNT = 5
    }

    override val values: Sequence<List<ProgressUiModel>>
        get() = sequenceOf(
            buildList {
                add(
                    HeaderProgressUiModel(
                        id = 99,
                        title = "Title",
                        progressPercent = 40,
                        progressPercentTitle = "40%",
                        color = app_color_neutral,
                    )
                )
                repeat(TEST_ITEM_COUNT) {
                    add(
                        ItemProgressUiModel(
                            id = it,
                            title = "Title",
                            subtitle = "Subtitle",
                            progressPercent = 40,
                            progressPercentTitle = "40%",
                            progressColor = app_color_neutral,
                        ),
                    )
                }
            }
        )
}
