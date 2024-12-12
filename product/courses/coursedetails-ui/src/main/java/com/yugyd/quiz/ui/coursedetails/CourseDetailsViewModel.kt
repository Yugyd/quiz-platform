/*
 *    Copyright 2023 Roman Likhachev
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.yugyd.quiz.ui.coursedetails

import androidx.lifecycle.SavedStateHandle
import com.yugyd.quiz.commonui.base.BaseViewModel
import com.yugyd.quiz.core.Logger
import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import com.yugyd.quiz.core.runCatch
import com.yugyd.quiz.domain.courses.CourseInteractor
import com.yugyd.quiz.domain.courses.model.CourseDetailModel
import com.yugyd.quiz.featuretoggle.domain.FeatureManager
import com.yugyd.quiz.featuretoggle.domain.model.FeatureToggle
import com.yugyd.quiz.ui.coursedetails.CourseDetailsView.Action
import com.yugyd.quiz.ui.coursedetails.CourseDetailsView.State
import com.yugyd.quiz.ui.coursedetails.CourseDetailsView.State.CourseDetailsDomainState
import com.yugyd.quiz.ui.coursedetails.CourseDetailsView.State.NavigationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CourseDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val featureManager: FeatureManager,
    private val courseInteractor: CourseInteractor,
    logger: Logger,
    dispatchersProvider: DispatchersProvider,
) :
    BaseViewModel<State, Action>(
        logger = logger,
        dispatchersProvider = dispatchersProvider,
        initialState = State(
            courseDetailsDomainState = CourseDetailsDomainState(
                payload = CourseDetailsArgs(savedStateHandle).courseDetailsPayload,
            ),
        ),
    ) {

    init {
        screenState = screenState.copy(
            courseTitle = screenState.courseDetailsDomainState.payload.courseTitle,
        )

        loadData()
    }

    override fun handleAction(action: Action) {
        when (action) {
            Action.OnBackPressed -> {
                screenState = screenState.copy(navigationState = NavigationState.Back)
            }

            Action.OnNavigationHandled -> {
                screenState = screenState.copy(navigationState = null)
            }

            Action.OnReportClicked -> {
                screenState = screenState.copy(
                    navigationState = NavigationState.NavigateToExternalPlatformReportError,
                )
            }

            Action.OnSnackbarDismissed -> {
                screenState = screenState.copy(showErrorMessage = false)
            }

            Action.OnTasksClicked -> onTasksClicked()
        }
    }

    private fun onTasksClicked() {
        val courseDetailModel = screenState.courseDetailsDomainState.courseDetailModel ?: return

        screenState = screenState.copy(
            navigationState = NavigationState.NavigateToTasks(
                id = courseDetailModel.id,
                title = courseDetailModel.name,
            )
        )
    }

    private fun loadData() {
        vmScopeErrorHandled.launch {
            val domainState = screenState.courseDetailsDomainState.copy(courseDetailModel = null)
            screenState = screenState.copy(
                isLoading = true,
                isWarning = false,
                courseDetailsDomainState = domainState,
            )

            runCatch(
                block = {
                    val isAiTasksEnabled = featureManager.isFeatureEnabled(FeatureToggle.AI_TASKS)
                    val courseDetails = courseInteractor.getCourseDetails(
                        courseId = screenState.courseDetailsDomainState.payload.courseId,
                    )
                    courseInteractor.setCurrentCourse(courseModel = courseDetails)
                    processData(courseDetails, isAiTasksEnabled)
                },
                catch = ::processDataError,
            )
        }
    }

    private fun processData(course: CourseDetailModel, isAiTasksEnabled: Boolean) {
        val domainState = screenState.courseDetailsDomainState.copy(
            courseDetailModel = course,
            isAiTasksEnabled = isAiTasksEnabled,
        )
        screenState = screenState.copy(
            isLoading = false,
            isWarning = false,
            courseDetailsDomainState = domainState,
            isActionsVisible = isAiTasksEnabled,
        )
    }

    private fun processDataError(error: Throwable) {
        val domainState = screenState.courseDetailsDomainState.copy(courseDetailModel = null)
        screenState = screenState.copy(
            isLoading = false,
            isWarning = true,
            courseDetailsDomainState = domainState,
            showErrorMessage = true,
        )
        processError(error)
    }
}
