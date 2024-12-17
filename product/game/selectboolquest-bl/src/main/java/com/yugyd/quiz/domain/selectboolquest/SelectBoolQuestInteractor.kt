package com.yugyd.quiz.domain.selectboolquest

import com.yugyd.quiz.domain.game.api.BaseQuestDomainModel
import com.yugyd.quiz.domain.game.api.QuestInteractor
import com.yugyd.quiz.domain.game.api.TrueAnswerResultModel
import com.yugyd.quiz.domain.game.api.model.HighlightModel
import com.yugyd.quiz.domain.game.api.model.Quest
import com.yugyd.quiz.domain.game.api.model.QuestType
import javax.inject.Inject

class SelectBoolQuestInteractor @Inject internal constructor(
    private val answerBuilder: AnswerBuilder,
) : QuestInteractor {

    override fun isTypeHandled(type: QuestType): Boolean {
        return type == QuestType.SELECT_BOOL
    }

    override fun isQuestHandled(quest: BaseQuestDomainModel): Boolean {
        return quest is SelectBoolQuestModel
    }

    override suspend fun isTrueAnswer(
        quest: BaseQuestDomainModel,
        selectedUserAnswers: Set<String>,
        enteredUserAnswer: String
    ): TrueAnswerResultModel {
        val isValid = selectedUserAnswers == (quest as SelectBoolQuestModel).trueAnswers
        return TrueAnswerResultModel(isValid = isValid)
    }

    override fun getQuestModel(quest: Quest): BaseQuestDomainModel {
        val answers = answerBuilder.buildAnswers()
        val trueAnswer = answerBuilder.buildTrueAnswer(quest.trueAnswer)

        val mappedQuest = SelectBoolQuestModel(
            id = quest.id,
            quest = quest.quest,
            image = quest.image,
            trueAnswers = setOf(trueAnswer),
            answers = answers,
        )

        return mappedQuest
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

        val trueAnswerIndexes = (quest as SelectBoolQuestModel).trueAnswers
            .map(quest.answers::indexOf)
            .toSet()

        return HighlightModel(
            state = highlightModel,
            trueAnswerIndexes = trueAnswerIndexes,
            selectedAnswerIndex = selectedUserAnswers.map(quest.answers::indexOf).toSet(),
        )
    }
}
