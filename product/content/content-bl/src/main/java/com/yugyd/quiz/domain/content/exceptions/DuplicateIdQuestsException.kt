package com.yugyd.quiz.domain.content.exceptions

import com.yugyd.quiz.domain.api.model.Quest

data class DuplicateIdQuestsException(
    override val message: String,
    val quests: Set<Quest>,
) : ContentVerificationException()
