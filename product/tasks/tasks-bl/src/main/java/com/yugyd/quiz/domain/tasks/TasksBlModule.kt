package com.yugyd.quiz.domain.tasks

import com.yugyd.quiz.domain.tasks.data.QueryFormatRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class TasksBlModule {

    @Binds
    internal abstract fun bindTasksInteractor(impl: TasksInteractorImpl): TasksInteractor

    @Binds
    internal abstract fun bindFilterInteractor(impl: FilterInteractorImpl): FilterInteractor

    @Binds
    internal abstract fun bindQueryUrlBuilder(impl: QuestQueryUrlBuilderImpl): QueryUrlBuilder

    @Binds
    internal abstract fun bindFormatRepository(impl: QueryFormatRepositoryImpl): QueryFormatRepository
}
