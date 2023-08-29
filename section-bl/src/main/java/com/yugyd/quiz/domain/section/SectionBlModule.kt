package com.yugyd.quiz.domain.section

import com.yugyd.quiz.domain.api.repository.QuestSource
import com.yugyd.quiz.domain.api.repository.SectionSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class SectionBlModule {

    @Provides
    fun provideSectionInteractor(
        questSource: QuestSource,
        sectionSource: SectionSource,
    ): SectionInteractor = SectionInteractorImpl(questSource, sectionSource)
}
