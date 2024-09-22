package com.yugyd.quiz.domain.tasks

import com.yugyd.quiz.domain.game.api.model.Quest

internal interface QueryUrlBuilder {
    fun buildUrl(quest: Quest, queryFormat: String): String
}