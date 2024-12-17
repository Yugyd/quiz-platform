package com.yugyd.quiz.ui.enterquest.di

import com.yugyd.quiz.ui.enterquest.EnterGameViewModelDelegate
import com.yugyd.quiz.ui.game.api.GameViewModelDelegate
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ViewModelComponent::class)
interface EnterGameViewModelModule {

    @Binds
    @IntoSet
    fun bindsGameViewModelDelegate(impl: EnterGameViewModelDelegate): GameViewModelDelegate
}
