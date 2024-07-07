package com.yugyd.quiz.domain.game.api

interface BaseQuestDomainModel {
    val id: Int
    val quest: String
    val trueAnswer: String
}
