package com.yugyd.quiz.ui.game.api.mapper

import com.yugyd.quiz.domain.game.api.BaseQuestDomainModel
import com.yugyd.quiz.ui.game.api.model.BaseQuestUiModel

interface BaseQuestUiMapper<T : BaseQuestDomainModel, R : BaseQuestUiModel, A : UiMapperArgs> {
    fun map(model: T, args: A): R
}
