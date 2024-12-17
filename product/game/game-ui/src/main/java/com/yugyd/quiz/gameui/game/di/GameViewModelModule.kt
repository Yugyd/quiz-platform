package com.yugyd.quiz.gameui.game.di

import com.yugyd.quiz.ui.enterquest.di.EnterGameViewModelModule
import com.yugyd.quiz.ui.hintenterquest.di.EnterWithHintGameViewModelModule
import com.yugyd.quiz.ui.selectmanualquest.di.SelectManualGameViewModelModule
import com.yugyd.quiz.ui.selectquest.di.SelectGameViewModelModule
import com.yugyd.quiz.ui.simplequest.di.SimpleGameViewModelModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module(
    includes = [
        SimpleGameViewModelModule::class,
        EnterGameViewModelModule::class,
        EnterWithHintGameViewModelModule::class,
        SelectManualGameViewModelModule::class,
        SelectGameViewModelModule::class,
    ]
)
@InstallIn(ViewModelComponent::class)
object GameViewModelModule
