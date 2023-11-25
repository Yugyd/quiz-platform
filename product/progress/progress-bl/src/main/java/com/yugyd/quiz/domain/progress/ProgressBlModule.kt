package com.yugyd.quiz.domain.progress

import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import com.yugyd.quiz.domain.api.repository.QuestSource
import com.yugyd.quiz.domain.api.repository.RecordSource
import com.yugyd.quiz.domain.api.repository.SectionSource
import com.yugyd.quiz.domain.api.repository.ThemeSource
import com.yugyd.quiz.domain.api.repository.TrainSource
import com.yugyd.quiz.domain.controller.RecordController
import com.yugyd.quiz.domain.controller.SectionController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProgressBlModule {

    @Singleton
    @Provides
    fun provideRecordInteractor(
        recordSource: RecordSource,
        questSource: QuestSource,
        sectionSource: SectionSource,
        trainSource: TrainSource,
        recordController: RecordController,
        sectionController: SectionController,
        dispatchersProvider: DispatchersProvider,
    ): RecordInteractor = RecordInteractorImpl(
        recordSource,
        questSource,
        sectionSource,
        trainSource,
        recordController,
        sectionController,
        dispatchersProvider,
    )

    @Singleton
    @Provides
    fun provideProgressInteractor(
        themeSource: ThemeSource,
        recordSource: RecordSource,
        dispatchersProvider: DispatchersProvider,
    ): ProgressInteractor = ProgressInteractorImpl(themeSource, recordSource, dispatchersProvider)
}
