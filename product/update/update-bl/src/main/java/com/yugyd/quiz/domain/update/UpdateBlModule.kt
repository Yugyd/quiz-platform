package com.yugyd.quiz.domain.update

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UpdateBlModule {

    @Binds
    internal abstract fun bindUpdateInteractor(impl: UpdateInteractorImpl): UpdateInteractor
}
