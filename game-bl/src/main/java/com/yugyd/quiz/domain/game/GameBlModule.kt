package com.yugyd.quiz.domain.game

import com.yugyd.quiz.domain.api.repository.ErrorSource
import com.yugyd.quiz.domain.api.repository.PreferencesSource
import com.yugyd.quiz.domain.api.repository.QuestSource
import com.yugyd.quiz.domain.api.repository.RecordSource
import com.yugyd.quiz.domain.api.repository.SectionSource
import com.yugyd.quiz.domain.api.repository.TrainSource
import com.yugyd.quiz.domain.controller.ErrorController
import com.yugyd.quiz.domain.controller.RecordController
import com.yugyd.quiz.domain.controller.SectionController
import com.yugyd.quiz.domain.utils.AbQuestParser
import com.yugyd.quiz.domain.utils.SeparatorParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GameBlModule {

    @Singleton
    @Provides
    fun provideGameInteractor(
        questSource: QuestSource,
        sectionSource: SectionSource,
        trainSource: TrainSource,
        recordSource: RecordSource,
        errorSource: ErrorSource,
        preferencesSource: PreferencesSource,
        abQuestParser: AbQuestParser,
        separatorParser: SeparatorParser,
        recordController: RecordController,
        sectionController: SectionController,
        errorController: ErrorController,
    ): GameInteractor = GameInteractorImpl(
        questSource,
        sectionSource,
        trainSource,
        recordSource,
        errorSource,
        preferencesSource,
        abQuestParser,
        separatorParser,
        recordController,
        sectionController,
        errorController,
    )
}
