package com.yugyd.quiz.domain.game

import com.yugyd.quiz.domain.enterai.di.EnterAiBlModule
import com.yugyd.quiz.domain.enterquest.di.EnterBlModule
import com.yugyd.quiz.domain.hintenterquest.di.EnterWithHintBlModule
import com.yugyd.quiz.domain.selectboolquest.di.SelectBoolBlModule
import com.yugyd.quiz.domain.selectmanualquest.di.SelectManualQuestBlModule
import com.yugyd.quiz.domain.selectquest.di.SelectQuestBlModule
import com.yugyd.quiz.domain.simplequest.di.SimpleBlModule
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module(
    includes = [
        SimpleBlModule::class,
        EnterBlModule::class,
        EnterWithHintBlModule::class,
        SelectManualQuestBlModule::class,
        SelectQuestBlModule::class,
        EnterAiBlModule::class,
        SelectBoolBlModule::class,
    ]
)
@InstallIn(ViewModelComponent::class)
abstract class GameBlModule {

    @ViewModelScoped
    @Binds
    internal abstract fun bindGameInteractor(impl: GameInteractorImpl): GameInteractor
}
