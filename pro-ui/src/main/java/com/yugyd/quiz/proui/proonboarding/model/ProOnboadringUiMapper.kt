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

package com.yugyd.quiz.proui.proonboarding.model

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.yugyd.quiz.proui.R
import com.yugyd.quiz.proui.proonboarding.ProOnboardingView.State
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import com.yugyd.quiz.uikit.R as uiKitR

class ProOnboadringUiMapper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val items by lazy {
        listOf(
            item(
                FIRST_ITEM,
                R.drawable.ic_school_48,
                R.string.title_extended_content,
                R.string.title_extended_content_info,
            ),
            item(
                TWO_ITEM,
                R.drawable.ic_rocket_launch_48,
                uiKitR.string.title_work_error,
                R.string.title_work_error_info
            ),
            item(
                THREE_ITEM,
                R.drawable.ic_ad_off_48,
                R.string.title_ad_off,
                R.string.title_ad_off_info
            )
        )
    }

    fun state() = State(items = items)

    private fun item(
        index: Int,
        @DrawableRes icon: Int,
        @StringRes title: Int,
        @StringRes subtitle: Int
    ) = ProOnboardingUiModel(
        id = index,
        icon = icon,
        title = context.getString(title),
        subtitle = context.getString(subtitle),
        showBackground = true,
    )

    companion object {
        private const val FIRST_ITEM = 1
        private const val TWO_ITEM = 2
        private const val THREE_ITEM = 3
    }
}
