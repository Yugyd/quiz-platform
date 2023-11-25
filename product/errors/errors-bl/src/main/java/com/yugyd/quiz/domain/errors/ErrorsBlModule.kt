package com.yugyd.quiz.domain.errors

import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import com.yugyd.quiz.domain.api.repository.ErrorSource
import com.yugyd.quiz.domain.api.repository.QuestSource
import com.yugyd.quiz.domain.utils.SeparatorParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ErrorsBlModule {

    @Singleton
    @Provides
    fun provideErrorInteractor(
        questSource: QuestSource,
        errorSource: ErrorSource,
        separatorParser: SeparatorParser,
        dispatchersProvider: DispatchersProvider,
    ): ErrorInteractor = ErrorInteractorImpl(
        questSource,
        errorSource,
        separatorParser,
        dispatchersProvider
    )
}
