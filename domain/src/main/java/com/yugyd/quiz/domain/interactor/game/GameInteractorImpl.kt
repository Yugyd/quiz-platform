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

package com.yugyd.quiz.domain.interactor.game

import com.yugyd.quiz.domain.controller.ErrorController
import com.yugyd.quiz.domain.controller.RecordController
import com.yugyd.quiz.domain.controller.SectionController
import com.yugyd.quiz.domain.exception.FinishGameException
import com.yugyd.quiz.domain.exception.RewardedGameException
import com.yugyd.quiz.domain.model.data.Quest
import com.yugyd.quiz.domain.model.data.Record
import com.yugyd.quiz.domain.model.game.ControlModel
import com.yugyd.quiz.domain.model.game.GameModel
import com.yugyd.quiz.domain.model.game.HighlightModel
import com.yugyd.quiz.domain.model.game.QuestModel
import com.yugyd.quiz.domain.model.payload.GameEndPayload
import com.yugyd.quiz.domain.model.payload.GamePayload
import com.yugyd.quiz.domain.model.share.Mode
import com.yugyd.quiz.domain.repository.PreferencesSource
import com.yugyd.quiz.domain.repository.content.QuestSource
import com.yugyd.quiz.domain.repository.user.ErrorSource
import com.yugyd.quiz.domain.repository.user.RecordSource
import com.yugyd.quiz.domain.repository.user.SectionSource
import com.yugyd.quiz.domain.repository.user.TrainSource
import com.yugyd.quiz.domain.utils.AbQuestParser
import com.yugyd.quiz.domain.utils.SeparatorParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GameInteractorImpl(
    private val questSource: QuestSource,
    private val sectionSource: SectionSource,
    private val trainSource: TrainSource,
    private val recordSource: RecordSource,
    private val errorSource: ErrorSource,
    private val preferencesSource: PreferencesSource,
    private val abQuestParser: AbQuestParser,
    private val separatorParser: SeparatorParser,
    private val recordController: RecordController,
    private val sectionController: SectionController,
    private val errorController: ErrorController
) : GameInteractor {

    private lateinit var payload: GamePayload
    private val gameMode get() = payload.mode
    private val themeId get() = payload.themeId
    private val sectionId get() = payload.sectionId
    private val record get() = payload.record

    private lateinit var data: GameModel
    private val questIds get() = data.questIds
    private val sectionIds get() = data.sectionIds
    private val trainIds get() = data.trainIds
    private val errorIds get() = data.errorIds
    private val gameSessionErrors get() = data.gameSessionErrors
    private val condition get() = data.condition
    private val point get() = data.point
    private val questCount get() = data.questCount
    private val isFinished get() = data.isFinished
    private val totalTime get() = data.endTime - data.startTime
    private val isShowRewarded get() = data.isShowRewarded
    private val isHaveRewardedItem get() = data.isHaveRewardedItem

    private val endPayload
        get() = GameEndPayload(
            mode = gameMode,
            themeId = themeId,
            oldRecord = record,
            point = point,
            count = questCount,
            errorQuestIds = gameSessionErrors.toList(),
            isRewardedSuccess = isHaveRewardedItem
        )

    override suspend fun startGame(payload: GamePayload) = withContext(Dispatchers.IO) {
        this@GameInteractorImpl.payload = payload
        data = GameModel(
            condition = getConditionValue(gameMode),
            startTime = System.currentTimeMillis()
        )

        val questIds = getQuestIds(
            mode = payload.mode,
            theme = payload.themeId,
            section = payload.sectionId,
            isSort = preferencesSource.isSortingQuest
        )

        data = data.copy(
            questIds = questIds,
            questCount = questIds.size
        )

        continueGame()
    }

    override suspend fun continueGame() = withContext(Dispatchers.IO) {
        isNext(questIds)

        if (condition > 0) {
            data = data.copy(steep = data.steep.inc())
            val nextQuest = nextQuest()
            val nextControl = nextControl()
            Pair(nextQuest, nextControl)
        } else {
            getFinishError()
        }
    }

    override suspend fun resultAnswer(
        quest: QuestModel,
        index: Int
    ) = withContext(Dispatchers.IO) {
        if (index == quest.trueAnswerIndex) {
            addRightQuest(gameMode, quest.id)
            incrementPoint()
            addSectionQuest(gameMode, quest.id)
            addTrainQuest(gameMode, quest.id)
            getHighlightModel(quest, index, true)
        } else {
            addErrorQuest(gameMode, quest.id)
            decrementCondition(gameMode)
            getHighlightModel(quest, index, false)
        }
    }

    override suspend fun finishGame() = withContext(Dispatchers.IO) {
        data = data.copy(
            isFinished = isFinished,
            endTime = System.currentTimeMillis()
        )

        saveData(gameMode)

        updateController()

        endPayload
    }

    override suspend fun firstFinishGame() {
        if (gameMode == Mode.TRAIN) {
            finishGame()
        }
    }

    override suspend fun onUserEarnedReward() = withContext(Dispatchers.IO) {
        data = data.copy(
            condition = condition.inc(),
            isHaveRewardedItem = true
        )
    }

    // BLOCK START: Start game
    private fun getConditionValue(mode: Mode) = when (mode) {
        Mode.ARCADE, Mode.MARATHON, Mode.TRAIN, Mode.ERROR -> LIFE_CONDITION
        Mode.NONE -> 0
    }

    private suspend fun getQuestIds(mode: Mode, theme: Int, section: Int, isSort: Boolean) =
        when (mode) {
            Mode.ARCADE -> getQuestIdsBySection(theme, section, isSort)
            Mode.MARATHON -> getQuestIdsByTheme(theme, isSort)
            Mode.TRAIN -> getQuestIdsByTrainMode(theme, isSort)
            Mode.ERROR -> getQuestIdsByErrors()
            Mode.NONE -> emptyList()
        }

    private suspend fun getQuestIdsByTheme(theme: Int, isSort: Boolean) =
        questSource.getQuestIds(theme, isSort)

    private suspend fun getQuestIdsBySection(theme: Int, section: Int, isSort: Boolean) =
        questSource.getQuestIdsBySection(theme, section, isSort)

    private suspend fun getQuestIdsByTrainMode(theme: Int, isSort: Boolean) = trainSource
        .getAll()
        .let { trainIds ->
            if (trainIds.isNotEmpty()) {
                getQuestIdsByTheme(theme, isSort).filterNot(trainIds::contains)
            } else {
                getQuestIdsByTheme(theme, isSort)
            }
        }

    private suspend fun getQuestIdsByErrors() = errorSource.getErrors().shuffled()

    private fun isNext(ids: List<Int>) {
        if (ids.isEmpty()) throw FinishGameException()
    }

    private suspend fun nextQuest() = getQuest(getQuestId())
        .let(::getQuestModel)
        .let(abQuestParser::parse)

    private fun nextControl() = ControlModel(
        mode = payload.mode,
        condition = data.condition,
        point = data.point,
        steep = data.steep,
        questCount = data.questCount
    )

    private fun getQuestId(): Int {
        val id = questIds.first()
        data = data.copy(questIds = questIds.minus(id))
        return id
    }

    private suspend fun getQuest(id: Int) = questSource
        .getQuest(id)
        .let(separatorParser::parse)

    // BLOCK END: Start game

    private fun getQuestModel(quest: Quest): QuestModel {
        val answers = listOf(
            quest.answer2,
            quest.answer3,
            quest.answer4,
            quest.answer5,
            quest.answer6,
            quest.answer7,
            quest.answer8
        )
            .filter { it.isNotEmpty() }
            .shuffled()
            .take(3)
            .plus(quest.trueAnswer)
            .shuffled()

        return QuestModel(
            id = quest.id,
            quest = quest.quest,
            trueAnswer = quest.trueAnswer,
            trueAnswerIndex = answers.indexOf(quest.trueAnswer),
            answers = answers
        )
    }

    // BLOCK START: Game process
    private fun incrementPoint() {
        data = data.copy(point = point.inc())
    }

    private fun decrementCondition(mode: Mode) {
        val fine = when (mode) {
            Mode.ARCADE, Mode.MARATHON, Mode.ERROR -> LIFE_FINE
            Mode.TRAIN -> INFINITY_FINE
            Mode.NONE -> 0
        }
        data = data.copy(condition = condition.minus(fine))
    }

    private suspend fun addErrorQuest(mode: Mode, id: Int) {
        if (mode != Mode.ERROR) {
            data = data.copy(errorIds = errorIds.plus(id))
            errorSource.addError(id)
        }

        data = data.copy(gameSessionErrors = gameSessionErrors.plus(id))
    }

    private suspend fun addRightQuest(mode: Mode, id: Int) {
        if (mode == Mode.ERROR) errorSource.removeError(id)
    }

    private fun addSectionQuest(mode: Mode, id: Int) {
        if (mode == Mode.ARCADE) {
            data = data.copy(sectionIds = sectionIds.plus(id))
        }
    }

    private fun addTrainQuest(mode: Mode, id: Int) {
        if (mode == Mode.TRAIN) {
            data = data.copy(trainIds = trainIds.plus(id))
        }
    }

    private fun getHighlightModel(quest: QuestModel, index: Int, isSuccess: Boolean) =
        HighlightModel(
            state = if (isSuccess) HighlightModel.State.TRUE else HighlightModel.State.FALSE,
            trueAnswerIndex = quest.trueAnswerIndex,
            selectedAnswerIndex = index
        )

    // BLOCK END: Game process

    // BLOCK START: Finish game

    private fun getFinishError(): Nothing {
        if (!isShowRewarded && gameMode != Mode.TRAIN) {
            data = data.copy(isShowRewarded = true)
            throw RewardedGameException()
        } else {
            throw FinishGameException()
        }
    }

    private suspend fun saveData(mode: Mode) {
        if (!isSaveRecord(mode)) return

        when (mode) {
            Mode.ARCADE -> saveSectionData()
            Mode.TRAIN -> saveTrainData()
            Mode.MARATHON, Mode.ERROR, Mode.NONE -> Unit
        }

        saveRecord(mode)
    }

    private fun isSaveRecord(mode: Mode) = when (mode) {
        Mode.TRAIN -> {
            point != 0
        }

        Mode.ARCADE, Mode.MARATHON, Mode.ERROR -> {
            point > record
        }

        Mode.NONE -> false
    }

    private suspend fun saveSectionData() {
        val resetIds = questSource.getQuestIdsBySection(themeId, sectionId)
        sectionSource.deleteSectionIds(resetIds)
        sectionSource.updateSectionIds(sectionIds.toList())
    }

    private suspend fun saveTrainData() = trainSource.addAll(trainIds.toList())

    private suspend fun saveRecord(mode: Mode) {
        if (!isSavedRecordMode(mode)) return

        val oldRecordModel = recordSource.getRecord(themeId, mode)
        val record = getPoint(mode)

        if (oldRecordModel != null) {
            val updateRecordModel = oldRecordModel.copy(
                record = record,
                totalTime = oldRecordModel.totalTime.plus(totalTime)
            )

            recordSource.updateRecord(updateRecordModel)
        } else {
            val recordModel = Record(
                themeId = themeId,
                mode = mode,
                record = record,
                totalTime = totalTime
            )

            recordSource.addRecord(recordModel)
        }
    }

    private suspend fun getPoint(mode: Mode) = when (mode) {
        Mode.ARCADE -> {
            val allIds = questSource.getQuestIds(themeId)
            sectionSource.getSectionTotalProgress(allIds.toIntArray())
        }

        Mode.TRAIN -> {
            val allIds = questSource.getQuestIds(themeId)
            trainSource.getTotalProgress(allIds.toIntArray())
        }

        Mode.MARATHON, Mode.ERROR, Mode.NONE -> {
            point
        }
    }

    private fun isSavedRecordMode(mode: Mode) = when (mode) {
        Mode.ARCADE, Mode.MARATHON, Mode.TRAIN -> true
        Mode.ERROR, Mode.NONE -> false
    }

    private suspend fun updateController() = withContext(Dispatchers.Main) {
        when (gameMode) {
            Mode.ARCADE -> {
                recordController.notifyListeners()
                sectionController.notifyListeners()
            }

            Mode.MARATHON, Mode.TRAIN -> {
                recordController.notifyListeners()
            }

            Mode.ERROR, Mode.NONE -> Unit
        }

        errorController.notifyListeners()
    }

    // BLOCK END: Finish game

    companion object {
        private const val LIFE_FINE = 1
        private const val INFINITY_FINE = 0

        private const val LIFE_CONDITION = 2
    }
}
