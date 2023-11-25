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

package com.yugyd.quiz.progressui.model

import android.content.Context
import com.yugyd.quiz.commonui.model.mode.ModeUiMapper
import com.yugyd.quiz.commonui.utils.ProgressColorUtils
import com.yugyd.quiz.domain.api.model.progress.Progress
import com.yugyd.quiz.domain.api.model.progress.TotalProgress
import com.yugyd.quiz.domain.api.model.specificprogress.ModeProgress
import com.yugyd.quiz.progressui.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import com.yugyd.quiz.uikit.R as uiKitR

class ProgressUiMapper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val degreeUiMapper: DegreeUiMapper,
    private val modeUiMapper: ModeUiMapper,
    private val progressColorUtils: ProgressColorUtils
) {

    fun map(models: List<Any>) = models.map { model ->
        when (model) {
            is TotalProgress -> model.toUi()
            is Progress -> model.toUi()
            is ModeProgress -> model.toUi()
            else -> throw IllegalArgumentException("Progress model no founded type")
        }
    }

    private fun TotalProgress.toUi() = run {
        val uiDegree = degreeUiMapper.map(degree)
        HeaderProgressUiModel(
            id = HEADER_ID,
            title = context.getString(uiDegree.title),
            progressPercent = percent,
            progressPercentTitle = context.getString(R.string.format_percent, percent),
            color = progressColorUtils.getProgressColor(percent)
        )
    }

    private fun Progress.toUi() = run {
        val uiDegree = degreeUiMapper.map(degree)
        ItemProgressUiModel(
            id = id,
            title = title,
            subtitle = context.getString(uiDegree.title),
            progressPercent = percent,
            progressPercentTitle = context.getString(R.string.format_percent, percent),
            progressColor = progressColorUtils.getProgressColor(percent)
        )
    }

    private fun ModeProgress.toUi() = run {
        val uiMode = modeUiMapper.map(mode)
        ItemProgressUiModel(
            id = id,
            title = context.getString(uiMode.title),
            subtitle = context.getString(uiKitR.string.format_record_progress, record, count),
            progressPercent = percent,
            progressPercentTitle = context.getString(R.string.format_percent, percent),
            progressColor = progressColorUtils.getProgressColor(percent)
        )
    }

    companion object {
        private const val HEADER_ID = -1
    }
}
