package com.yugyd.quiz.core.searchutils.data

import com.yugyd.quiz.core.Logger
import com.yugyd.quiz.core.searchutils.QueryUrlBuilder
import com.yugyd.quiz.core.searchutils.SearchQuest
import java.util.IllegalFormatException
import javax.inject.Inject

internal class QuestQueryUrlBuilderImpl @Inject constructor(
    private val logger: Logger,
) : QueryUrlBuilder {

    override fun buildUrl(quest: SearchQuest, queryFormat: String): String {
        if (queryFormat.isEmpty()) {
            return ""
        }

        return try {
            String.format(queryFormat, quest.quest, quest.trueAnswer)
        } catch (error: IllegalFormatException) {
            logger.logError(TAG, error)
            ""
        }
    }

    companion object {
        private const val TAG = "QuestQueryUrlBuilderImpl"
    }
}
