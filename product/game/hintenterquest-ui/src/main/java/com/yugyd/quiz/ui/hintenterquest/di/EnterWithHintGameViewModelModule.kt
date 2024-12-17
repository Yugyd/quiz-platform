package com.yugyd.quiz.ui.hintenterquest.di

import com.yugyd.quiz.ui.game.api.GameViewModelDelegate
import com.yugyd.quiz.ui.hintenterquest.EnterWithHintGameViewModelDelegate
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ViewModelComponent::class)
interface EnterWithHintGameViewModelModule {

    @Binds
    @IntoSet
    fun bindsGameViewModelDelegate(impl: EnterWithHintGameViewModelDelegate): GameViewModelDelegate
}
