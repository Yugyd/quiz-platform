package com.yugyd.quiz.gameui.game

import com.yugyd.quiz.ui.enterquest.di.EnterGameViewModelModule
import com.yugyd.quiz.ui.simplequest.di.SimpleGameViewModelModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module(
    includes = [
        SimpleGameViewModelModule::class,
        EnterGameViewModelModule::class,
    ]
)
@InstallIn(ViewModelComponent::class)
object GameViewModelModule
