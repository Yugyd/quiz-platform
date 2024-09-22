package com.yugyd.quiz.domain.tasks

import com.yugyd.quiz.core.Logger
import com.yugyd.quiz.domain.game.api.model.Quest
import java.util.IllegalFormatException
import javax.inject.Inject

internal class QuestQueryUrlBuilderImpl @Inject constructor(
    private val logger: Logger,
) : QueryUrlBuilder {

    override fun buildUrl(quest: Quest, queryFormat: String): String {
        if (queryFormat.isEmpty()) {
            return ""
        }

        return try {
            String.format(queryFormat, quest.quest)
        } catch (error: IllegalFormatException) {
            logger.logError(TAG, error)
            ""
        }
    }

    companion object {
        private const val TAG = "QuestQueryUrlBuilderImpl"
    }
}
