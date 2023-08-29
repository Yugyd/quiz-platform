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

package com.yugyd.quiz.domain.progress

import com.yugyd.quiz.domain.api.repository.QuestSource
import com.yugyd.quiz.domain.api.repository.RecordSource
import com.yugyd.quiz.domain.api.repository.SectionSource
import com.yugyd.quiz.domain.api.repository.TrainSource
import com.yugyd.quiz.domain.controller.RecordController
import com.yugyd.quiz.domain.controller.SectionController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecordInteractorImpl(
    private val recordSource: RecordSource,
    private val questSource: QuestSource,
    private val sectionSource: SectionSource,
    private val trainSource: TrainSource,
    private val recordController: RecordController,
    private val sectionController: SectionController
) : RecordInteractor {

    override fun isRecordReset(progressPercent: Int) = progressPercent == FULL_PERCENT

    override suspend fun resetRecord(theme: Int) {
        withContext(Dispatchers.IO) {
            recordSource.deleteRecord(theme)

            val allIds = questSource.getQuestIds(theme)
            trainSource.deleteAll(allIds)
            sectionSource.deleteSectionIds(allIds)

            updateController()
        }
    }

    private suspend fun updateController() = withContext(Dispatchers.Main) {
        recordController.notifyListeners()
        sectionController.notifyListeners()
    }

    companion object {
        private const val FULL_PERCENT = 100
    }
}
