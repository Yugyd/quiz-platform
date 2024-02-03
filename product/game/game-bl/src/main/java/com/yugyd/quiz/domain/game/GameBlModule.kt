package com.yugyd.quiz.domain.game

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class GameBlModule {

    @ViewModelScoped
    @Binds
    internal abstract fun bindGameInteractor(
        impl: GameInteractorImpl,
    ): GameInteractor
}
