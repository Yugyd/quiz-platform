package com.yugyd.quiz.domain.simplequest

import com.yugyd.quiz.domain.game.api.BaseQuestDomainModel
import com.yugyd.quiz.domain.game.api.QuestInteractor
import com.yugyd.quiz.domain.game.api.TrueAnswerResultModel
import com.yugyd.quiz.domain.game.api.model.HighlightModel
import com.yugyd.quiz.domain.game.api.model.Quest
import com.yugyd.quiz.domain.game.api.model.QuestType
import javax.inject.Inject

class SimpleQuestInteractor @Inject constructor(
    private val abQuestParser: IAbQuestParser,
) : QuestInteractor {

    override fun isTypeHandled(type: QuestType): Boolean {
        return type == QuestType.SIMPLE
    }

    override fun isQuestHandled(quest: BaseQuestDomainModel): Boolean {
        return quest is SimpleQuestModel
    }

    override suspend fun isTrueAnswer(
        quest: BaseQuestDomainModel,
        selectedUserAnswers: Set<String>,
        enteredUserAnswer: String
    ): TrueAnswerResultModel {
        val isValid = selectedUserAnswers == (quest as SimpleQuestModel).trueAnswers
        return TrueAnswerResultModel(isValid = isValid)
    }

    override fun getQuestModel(quest: Quest): BaseQuestDomainModel {
        val answers = listOf(
            quest.answer2,
            quest.answer3,
            quest.answer4,
            quest.answer5,
            quest.answer6,
            quest.answer7,
            quest.answer8,
        )
            .filter { it.isNotEmpty() }
            .shuffled()
            .take(3)
            .plus(quest.trueAnswer)
            .shuffled()
        val simpleQuest = SimpleQuestModel(
            id = quest.id,
            quest = quest.quest,
            trueAnswers = setOf(quest.trueAnswer),
            answers = answers,
        )
        return abQuestParser.parse(simpleQuest)
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

        val trueAnswerIndexes = (quest as SimpleQuestModel).trueAnswers
            .map(quest.answers::indexOf)
            .toSet()

        return HighlightModel(
            state = highlightModel,
            trueAnswerIndexes = trueAnswerIndexes,
            selectedAnswerIndex = selectedUserAnswers.map(quest.answers::indexOf).toSet(),
        )
    }
}
