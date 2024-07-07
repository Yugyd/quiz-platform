package com.yugyd.quiz.domain.content.api

import com.yugyd.quiz.domain.api.model.Theme
import com.yugyd.quiz.domain.game.api.model.Quest

data class ContentDataModel(
    val quests: List<Quest>,
    val themes: List<Theme>,
)
