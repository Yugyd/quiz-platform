package com.yugyd.quiz.domain.errors

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ErrorsBlModule {

    @Binds
    internal abstract fun bindErrorInteractor(
        impl: ErrorInteractorImpl,
    ): ErrorInteractor
}
