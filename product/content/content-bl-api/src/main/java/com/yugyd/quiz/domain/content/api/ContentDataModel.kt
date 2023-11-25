package com.yugyd.quiz.domain.content.api

import com.yugyd.quiz.domain.api.model.Quest
import com.yugyd.quiz.domain.api.model.Theme

data class ContentDataModel(
    val quests: List<Quest>,
    val themes: List<Theme>,
)
