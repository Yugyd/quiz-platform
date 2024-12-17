package com.yugyd.quiz.domain.selectmanualquest

import com.yugyd.quiz.domain.game.api.BaseQuestDomainModel
import com.yugyd.quiz.domain.game.api.QuestInteractor
import com.yugyd.quiz.domain.game.api.TrueAnswerResultModel
import com.yugyd.quiz.domain.game.api.model.HighlightModel
import com.yugyd.quiz.domain.game.api.model.Quest
import com.yugyd.quiz.domain.game.api.model.QuestType
import javax.inject.Inject

class SelectManualQuestInteractor @Inject constructor() : QuestInteractor {

    override fun isTypeHandled(type: QuestType): Boolean {
        return type == QuestType.SELECT_MANUAL
    }

    override fun isQuestHandled(quest: BaseQuestDomainModel): Boolean {
        return quest is SelectManualQuestModel
    }

    override suspend fun isTrueAnswer(
        quest: BaseQuestDomainModel,
        selectedUserAnswers: Set<String>,
        enteredUserAnswer: String
    ): TrueAnswerResultModel {
        val isValid = selectedUserAnswers == (quest as SelectManualQuestModel).trueAnswers
        return TrueAnswerResultModel(isValid = isValid)
    }

    override fun getQuestModel(quest: Quest): BaseQuestDomainModel {
        val trueAnswers = setOf(
            quest.trueAnswer,
            quest.answer2,
        )

        val answers = listOf(
            quest.answer3,
            quest.answer4,
            quest.answer5,
            quest.answer6,
            quest.answer7,
            quest.answer8,
        )
            .filter { it.isNotEmpty() }
            .shuffled()
            .plus(trueAnswers)
            .shuffled()

        val simpleQuest = SelectManualQuestModel(
            id = quest.id,
            quest = quest.quest,
            trueAnswers = trueAnswers,
            answers = answers,
        )
        return simpleQuest
    }

    override fun getHighlightModel(
        quest: BaseQuestDomainModel,
        selectedUserAnswers: Set<String>,
        isSuccess: Boolean
    ): HighlightModel {
        val highlightModel = if (isSuccess) {
            HighlightModel.State.TRUE
        } else {
            HighlightModel.State.FALSE
        }

        val trueAnswerIndexes = (quest as SelectManualQuestModel).trueAnswers
            .map(quest.answers::indexOf)
            .toSet()

        return HighlightModel(
            state = highlightModel,
            trueAnswerIndexes = trueAnswerIndexes,
            selectedAnswerIndex = selectedUserAnswers.map(quest.answers::indexOf).toSet(),
        )
    }
}
