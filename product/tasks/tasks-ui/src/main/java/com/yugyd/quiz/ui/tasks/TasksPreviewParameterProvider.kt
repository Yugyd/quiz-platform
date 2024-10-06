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

package com.yugyd.quiz.ui.tasks

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.yugyd.quiz.domain.tasks.model.TaskModel

internal class TasksPreviewParameterProvider : PreviewParameterProvider<List<TaskModel>> {

    companion object {
        private const val TEST_ITEM_COUNT = 5
    }

    override val values: Sequence<List<TaskModel>>
        get() = sequenceOf(
            buildList {
                repeat(TEST_ITEM_COUNT) {
                    add(
                        TaskModel(
                            id = it,
                            quest = "Quest $it",
                            trueAnswer = "True answer $it",
                            queryLink = "example.com",
                        )
                    )
                }
            }
        )
}
