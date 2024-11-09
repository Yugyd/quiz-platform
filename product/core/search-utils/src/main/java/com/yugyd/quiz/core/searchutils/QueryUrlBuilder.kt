package com.yugyd.quiz.core.searchutils

interface QueryUrlBuilder {
    fun buildUrl(quest: SearchQuest, queryFormat: String): String
}
