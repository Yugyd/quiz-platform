package com.yugyd.quiz.domain.game.model

import com.yugyd.quiz.domain.api.model.game.ControlModel
import com.yugyd.quiz.domain.game.api.BaseQuestDomainModel

data class GameState(
    val quest: BaseQuestDomainModel,
    val control: ControlModel,
)
