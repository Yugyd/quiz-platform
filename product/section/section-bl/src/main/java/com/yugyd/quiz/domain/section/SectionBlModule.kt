package com.yugyd.quiz.domain.section

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class SectionBlModule {

    @Binds
    internal abstract fun bindSectionInteractor(
        impl: SectionInteractorImpl,
    ): SectionInteractor
}
