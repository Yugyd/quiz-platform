package com.yugyd.quiz.domain.courses

import com.yugyd.quiz.domain.courses.data.SearchCourseLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class CoursesBlModule {

    @Binds
    internal abstract fun bindSearchCourseLocalSource(
        impl: SearchCourseLocalDataSource,
    ): SearchCourseLocalSource

    @Binds
    internal abstract fun bindCourseInteractor(
        impl: CourseInteractorImpl,
    ): CourseInteractor

    @Binds
    internal abstract fun bindSearchCourseInteractor(
        impl: SearchCourseInteractorImpl,
    ): SearchCourseInteractor
}
