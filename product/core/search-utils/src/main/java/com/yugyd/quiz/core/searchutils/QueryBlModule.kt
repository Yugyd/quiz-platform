package com.yugyd.quiz.core.searchutils

import com.yugyd.quiz.core.searchutils.data.QueryFormatRepositoryImpl
import com.yugyd.quiz.core.searchutils.data.QuestQueryUrlBuilderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class QueryBlModule {

    @Binds
    internal abstract fun bindQueryUrlBuilder(impl: QuestQueryUrlBuilderImpl): QueryUrlBuilder

    @Binds
    internal abstract fun bindFormatRepository(impl: QueryFormatRepositoryImpl): QueryFormatRepository
}
