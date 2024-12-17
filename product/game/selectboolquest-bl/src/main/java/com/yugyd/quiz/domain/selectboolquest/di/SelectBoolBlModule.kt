package com.yugyd.quiz.domain.selectboolquest.di

import com.yugyd.quiz.domain.game.api.QuestInteractor
import com.yugyd.quiz.domain.selectboolquest.AnswerBuilder
import com.yugyd.quiz.domain.selectboolquest.AnswerBuilderImpl
import com.yugyd.quiz.domain.selectboolquest.SelectBoolQuestInteractor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ViewModelComponent::class)
abstract class SelectBoolBlModule {

    @Binds
    internal abstract fun bindsAnswerBuilder(impl: AnswerBuilderImpl): AnswerBuilder

    @Binds
    @IntoSet
    abstract fun bindsQuestInteractor(impl: SelectBoolQuestInteractor): QuestInteractor
}
