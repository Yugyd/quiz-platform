package com.yugyd.quiz.domain.selectquest.di

import com.yugyd.quiz.domain.game.api.QuestInteractor
import com.yugyd.quiz.domain.selectquest.SelectQuestInteractor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ViewModelComponent::class)
interface SelectQuestBlModule {

    @Binds
    @IntoSet
    fun bindsQuestInteractor(impl: SelectQuestInteractor): QuestInteractor
}
