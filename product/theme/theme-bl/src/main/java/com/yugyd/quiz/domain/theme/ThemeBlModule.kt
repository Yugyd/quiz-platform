package com.yugyd.quiz.domain.theme

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ThemeBlModule {

    @Binds
    internal abstract fun bindThemeInteractor(impl: ThemeInteractorImpl): ThemeInteractor
}
