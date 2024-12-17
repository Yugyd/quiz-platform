package com.yugyd.quiz.domain.selectmanualquest.di

import com.yugyd.quiz.domain.game.api.QuestInteractor
import com.yugyd.quiz.domain.selectmanualquest.SelectManualQuestInteractor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ViewModelComponent::class)
interface SelectManualQuestBlModule {

    @Binds
    @IntoSet
    fun bindsQuestInteractor(impl: SelectManualQuestInteractor): QuestInteractor
}
