package com.yugyd.quiz.domain.enterquest.di

import com.yugyd.quiz.domain.enterquest.EnterQuestInteractor
import com.yugyd.quiz.domain.game.api.QuestInteractor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ViewModelComponent::class)
interface EnterBlModule {

    @Binds
    @IntoSet
    fun bindsQuestInteractor(impl: EnterQuestInteractor): QuestInteractor
}
