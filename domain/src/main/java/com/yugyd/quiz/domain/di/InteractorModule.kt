/*
 *    Copyright 2023 Roman Likhachev
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.yugyd.quiz.domain.di

import com.yugyd.quiz.domain.controller.ErrorController
import com.yugyd.quiz.domain.controller.RecordController
import com.yugyd.quiz.domain.controller.SectionController
import com.yugyd.quiz.domain.interactor.error.ErrorInteractor
import com.yugyd.quiz.domain.interactor.error.ErrorInteractorImpl
import com.yugyd.quiz.domain.interactor.game.GameInteractor
import com.yugyd.quiz.domain.interactor.game.GameInteractorImpl
import com.yugyd.quiz.domain.interactor.options.OptionsInteractor
import com.yugyd.quiz.domain.interactor.options.OptionsInteractorImpl
import com.yugyd.quiz.domain.interactor.progress.ProgressInteractor
import com.yugyd.quiz.domain.interactor.progress.ProgressInteractorImpl
import com.yugyd.quiz.domain.interactor.quest.QuestInteractor
import com.yugyd.quiz.domain.interactor.quest.QuestInteractorImpl
import com.yugyd.quiz.domain.interactor.record.RecordInteractor
import com.yugyd.quiz.domain.interactor.record.RecordInteractorImpl
import com.yugyd.quiz.domain.interactor.section.SectionInteractor
import com.yugyd.quiz.domain.interactor.section.SectionInteractorImpl
import com.yugyd.quiz.domain.interactor.theme.ThemeInteractor
import com.yugyd.quiz.domain.interactor.theme.ThemeInteractorImpl
import com.yugyd.quiz.domain.interactor.update.UpdateInteractor
import com.yugyd.quiz.domain.interactor.update.UpdateInteractorImpl
import com.yugyd.quiz.domain.repository.PreferencesSource
import com.yugyd.quiz.domain.repository.content.QuestSource
import com.yugyd.quiz.domain.repository.content.ThemeSource
import com.yugyd.quiz.domain.repository.user.ErrorSource
import com.yugyd.quiz.domain.repository.user.RecordSource
import com.yugyd.quiz.domain.repository.user.SectionSource
import com.yugyd.quiz.domain.repository.user.TrainSource
import com.yugyd.quiz.domain.utils.AbQuestParser
import com.yugyd.quiz.domain.utils.SeparatorParser
import com.yugyd.quiz.featuretoggle.domain.RemoteConfigRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class InteractorModule {

    @Singleton
    @Provides
    fun provideThemeInteractor(
        themeSource: ThemeSource,
        recordSource: RecordSource
    ): ThemeInteractor = ThemeInteractorImpl(themeSource, recordSource)

    @Singleton
    @Provides
    fun provideRecordInteractor(
        recordSource: RecordSource,
        questSource: QuestSource,
        sectionSource: SectionSource,
        trainSource: TrainSource,
        recordController: RecordController,
        sectionController: SectionController
    ): RecordInteractor = RecordInteractorImpl(
        recordSource,
        questSource,
        sectionSource,
        trainSource,
        recordController,
        sectionController
    )

    @Singleton
    @Provides
    fun provideErrorInteractor(
        questSource: QuestSource,
        errorSource: ErrorSource,
        separatorParser: SeparatorParser
    ): ErrorInteractor = ErrorInteractorImpl(questSource, errorSource, separatorParser)

    @Singleton
    @Provides
    fun provideProgressInteractor(
        themeSource: ThemeSource,
        recordSource: RecordSource
    ): ProgressInteractor = ProgressInteractorImpl(themeSource, recordSource)

    @Singleton
    @Provides
    fun provideOptionsInteractor(preferencesDataSource: PreferencesSource): OptionsInteractor =
        OptionsInteractorImpl(preferencesDataSource)

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
        errorController: ErrorController
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
        errorController
    )

    @Singleton
    @Provides
    fun provideQuestInteractor(questSource: QuestSource): QuestInteractor =
        QuestInteractorImpl(questSource)

    @Singleton
    @Provides
    fun provideSectionInteractor(
        questSource: QuestSource,
        sectionSource: SectionSource
    ): SectionInteractor = SectionInteractorImpl(questSource, sectionSource)

    @Singleton
    @Provides
    fun provideUpdateInteractor(
        remoteConfigRepository: RemoteConfigRepository
    ): UpdateInteractor = UpdateInteractorImpl(remoteConfigRepository)
}
