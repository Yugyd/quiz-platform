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

package com.yugyd.quiz.update.model

import android.content.Context
import com.yugyd.quiz.featuretoggle.domain.model.update.UpdateConfig
import com.yugyd.quiz.update.R
import com.yugyd.quiz.update.UpdateView.State
import com.yugyd.quiz.update.UpdateView.State.UpdateConfigUiModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class UpdateUiMapper @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun map(updateConfig: UpdateConfig?): State {
        return State(
            updateConfig = UpdateConfigUiModel(
                title = updateConfig?.title ?: context.getString(R.string.update_title_update),
                message = updateConfig?.message
                    ?: context.getString(R.string.update_title_update_description),
                buttonTitle = updateConfig?.buttonTitle
                    ?: context.getString(R.string.update_action_update),
            ),
            isLoading = false,
        )
    }

    fun makeDefaultState(): State {
        return State(
            updateConfig = UpdateConfigUiModel(
                buttonTitle = context.getString(R.string.update_action_update),
                message = context.getString(R.string.update_title_update_description),
                title = context.getString(R.string.update_title_update),
            ),
            isLoading = false,
        )
    }
}
