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

package com.yugyd.quiz.ui.end.progressend.model

import android.content.Context
import com.yugyd.quiz.featuretoggle.domain.model.telegram.TelegramConfig
import com.yugyd.quiz.ui.end.R
import com.yugyd.quiz.ui.end.progressend.ProgressEndView.State
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class ProgressEndMapper @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun map(currentState: State, config: TelegramConfig?): State {
        return if (config?.gameEnd != null) {
            currentState.copy(
                title = config.gameEnd.title,
                message = config.gameEnd.message,
                actionButtonTitle = config.gameEnd.buttonTitle,
                actionButtonType = State.ActionButtonType.TELEGRAM,
            )
        } else {
            currentState.copy(
                title = context.getString(R.string.title_well),
                message = context.getString(R.string.msg_new_record),
                actionButtonTitle = context.getString(R.string.action_leave_rate),
                actionButtonType = State.ActionButtonType.RATE,
            )
        }
    }
}