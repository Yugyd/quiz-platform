package com.yugyd.quiz.domain.theme

import com.yugyd.quiz.domain.api.repository.RecordSource
import com.yugyd.quiz.domain.api.repository.ThemeSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ThemeBlModule {

    @Singleton
    @Provides
    fun provideThemeInteractor(
        themeSource: ThemeSource,
        recordSource: RecordSource,
    ): ThemeInteractor = ThemeInteractorImpl(themeSource, recordSource)
}
