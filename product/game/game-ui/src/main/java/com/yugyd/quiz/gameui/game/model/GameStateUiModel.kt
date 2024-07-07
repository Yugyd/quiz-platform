package com.yugyd.quiz.gameui.game.model

import com.yugyd.quiz.ui.game.api.model.BaseQuestUiModel

internal data class GameStateUiModel(
    val control: ControlUiModel,
    val quest: BaseQuestUiModel,
)
