/*
 * Copyright 2024 Roman Likhachev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yugyd.quiz.ui.aisettings

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.yugyd.quiz.ui.aisettings.model.AiConnectionUiModel

internal class AiSettingsPreviewParameterProvider :
    PreviewParameterProvider<List<AiConnectionUiModel>> {

    companion object {
        private const val TEST_ITEM_COUNT = 5
    }

    override val values: Sequence<List<AiConnectionUiModel>>
        get() = sequenceOf(
            buildList {
                repeat(TEST_ITEM_COUNT) {
                    val isActive = it == 0
                    add(
                        AiConnectionUiModel(
                            id = it.toString(),
                            name = "Name $it",
                            status = if (isActive) "Connected" else null,
                            isActive = isActive,
                        )
                    )
                }
            }
        )
}
