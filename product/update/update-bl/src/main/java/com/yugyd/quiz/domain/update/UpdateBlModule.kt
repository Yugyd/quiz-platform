package com.yugyd.quiz.domain.update

import com.yugyd.quiz.featuretoggle.domain.RemoteConfigRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UpdateBlModule {

    @Singleton
    @Provides
    fun provideUpdateInteractor(
        remoteConfigRepository: RemoteConfigRepository,
    ): UpdateInteractor = UpdateInteractorImpl(remoteConfigRepository)
}
