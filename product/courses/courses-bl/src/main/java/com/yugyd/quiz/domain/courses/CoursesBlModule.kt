package com.yugyd.quiz.domain.courses

import com.yugyd.quiz.domain.courses.data.CourseInMemoryDataSource
import com.yugyd.quiz.domain.courses.data.SearchCourseLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CoursesBlModule {

    @Binds
    internal abstract fun bindSearchCourseLocalSource(
        impl: SearchCourseLocalDataSource,
    ): SearchCourseLocalSource

    @Binds
    internal abstract fun bindCourseInMemorySource(
        impl: CourseInMemoryDataSource,
    ): CourseInMemorySource

    @Binds
    internal abstract fun bindCourseInteractor(
        impl: CourseInteractorImpl,
    ): CourseInteractor

    @Binds
    internal abstract fun bindSearchCourseInteractor(
        impl: SearchCourseInteractorImpl,
    ): SearchCourseInteractor
}
