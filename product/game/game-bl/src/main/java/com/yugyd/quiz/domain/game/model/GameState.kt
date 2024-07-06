package com.yugyd.quiz.domain.game.model

import com.yugyd.quiz.domain.api.model.game.ControlModel
import com.yugyd.quiz.domain.api.model.game.QuestModel

data class GameState(
    val quest: QuestModel,
    val control: ControlModel,
)
