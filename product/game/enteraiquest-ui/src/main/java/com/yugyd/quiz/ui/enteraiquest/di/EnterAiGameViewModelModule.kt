package com.yugyd.quiz.ui.enteraiquest.di

import com.yugyd.quiz.ui.enteraiquest.EnterAiGameViewModelDelegate
import com.yugyd.quiz.ui.game.api.GameViewModelDelegate
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ViewModelComponent::class)
interface EnterAiGameViewModelModule {

    @Binds
    @IntoSet
    fun bindsGameViewModelDelegate(impl: EnterAiGameViewModelDelegate): GameViewModelDelegate
}
