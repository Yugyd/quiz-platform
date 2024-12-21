package com.yugyd.quiz.domain.enterai

import com.yugyd.quiz.core.Logger
import com.yugyd.quiz.core.result
import com.yugyd.quiz.domain.game.api.BaseQuestDomainModel
import com.yugyd.quiz.domain.game.api.QuestInteractor
import com.yugyd.quiz.domain.game.api.TrueAnswerResultModel
import com.yugyd.quiz.domain.game.api.model.HighlightModel
import com.yugyd.quiz.domain.game.api.model.Quest
import com.yugyd.quiz.domain.game.api.model.QuestType
import com.yugyd.quiz.domain.questai.AiQuestInteractor
import javax.inject.Inject

class EnterAiQuestInteractor @Inject constructor(
    private val aiQuestInteractor: AiQuestInteractor,
    private val logger: Logger,
) : QuestInteractor {

    override fun isTypeHandled(type: QuestType): Boolean {
        return type == QuestType.ENTER_AI
    }

    override fun isQuestHandled(quest: BaseQuestDomainModel): Boolean {
        return quest is EnterAiQuestModel
    }

    override suspend fun isTrueAnswer(
        quest: BaseQuestDomainModel,
        selectedUserAnswers: Set<String>,
        enteredUserAnswer: String
    ): TrueAnswerResultModel {
        quest as EnterAiQuestModel

        return result {
            val aiResult = aiQuestInteractor.verifyTask(
                userAnswer = enteredUserAnswer,
                trueAnswer = quest.trueAnswers.first(),
                quest = quest.quest,
            )

            TrueAnswerResultModel(
                isValid = aiResult.isValid,
                trueAnswerDescription = aiResult.aiTrueAnswer,
            )
        }
            .getOrElse {
                logger.logError(it)

                TrueAnswerResultModel(
                    isValid = false,
                    trueAnswerDescription = "",
                )
            }
    }

    override fun getQuestModel(quest: Quest): BaseQuestDomainModel {
        return EnterAiQuestModel(
            id = quest.id,
            quest = quest.quest,
            trueAnswers = setOf(quest.trueAnswer),
        )
    }

    override fun getHighlightModel(
        quest: BaseQuestDomainModel,
        selectedUserAnswers: Set<String>,
        isSuccess: Boolean,
    ): HighlightModel {
        val highlightModel = if (isSuccess) {
            HighlightModel.State.TRUE
        } else {
            HighlightModel.State.FALSE
        }

        return HighlightModel(state = highlightModel)
    }
}