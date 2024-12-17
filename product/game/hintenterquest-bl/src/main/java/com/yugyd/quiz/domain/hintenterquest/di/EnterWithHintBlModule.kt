package com.yugyd.quiz.domain.hintenterquest.di

import com.yugyd.quiz.domain.game.api.QuestInteractor
import com.yugyd.quiz.domain.hintenterquest.EnterWithHintQuestInteractor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ViewModelComponent::class)
interface EnterWithHintBlModule {

    @Binds
    @IntoSet
    fun bindsQuestInteractor(impl: EnterWithHintQuestInteractor): QuestInteractor
}
