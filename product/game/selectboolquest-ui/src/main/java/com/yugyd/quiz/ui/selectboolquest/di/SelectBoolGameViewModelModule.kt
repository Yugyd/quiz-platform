package com.yugyd.quiz.ui.selectboolquest.di

import com.yugyd.quiz.ui.game.api.GameViewModelDelegate
import com.yugyd.quiz.ui.selectboolquest.viewmodel.SelectBoolGameViewModelDelegate
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ViewModelComponent::class)
interface SelectBoolGameViewModelModule {

    @Binds
    @IntoSet
    fun bindsGameViewModelDelegate(impl: SelectBoolGameViewModelDelegate): GameViewModelDelegate
}
