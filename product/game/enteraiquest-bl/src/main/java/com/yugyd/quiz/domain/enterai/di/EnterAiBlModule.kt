package com.yugyd.quiz.domain.enterai.di

import com.yugyd.quiz.domain.enterai.EnterAiQuestInteractor
import com.yugyd.quiz.domain.game.api.QuestInteractor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ViewModelComponent::class)
interface EnterAiBlModule {

    @Binds
    @IntoSet
    fun bindsQuestInteractor(impl: EnterAiQuestInteractor): QuestInteractor
}
