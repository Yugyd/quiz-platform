package com.yugyd.quiz.domain.game

import com.yugyd.quiz.domain.enterquest.di.EnterBlModule
import com.yugyd.quiz.domain.simplequest.di.SimpleBlModule
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module(
    includes = [
        SimpleBlModule::class,
        EnterBlModule::class,
    ]
)
@InstallIn(ViewModelComponent::class)
abstract class GameBlModule {

    @ViewModelScoped
    @Binds
    internal abstract fun bindGameInteractor(
        impl: GameInteractorImpl,
    ): GameInteractor
}
