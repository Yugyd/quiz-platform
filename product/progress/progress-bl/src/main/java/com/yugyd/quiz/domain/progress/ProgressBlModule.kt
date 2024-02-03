package com.yugyd.quiz.domain.progress

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ProgressBlModule {

    @Binds
    internal abstract fun bindRecordInteractor(
        impl: RecordInteractorImpl,
    ): RecordInteractor

    @Binds
    internal abstract fun bindProgressInteractor(
        impl: ProgressInteractorImpl,
    ): ProgressInteractor
}
