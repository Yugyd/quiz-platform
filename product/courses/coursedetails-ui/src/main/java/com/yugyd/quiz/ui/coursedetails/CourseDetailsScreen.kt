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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Report
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mikepenz.markdown.m3.Markdown
import com.yugyd.quiz.domain.api.payload.AiTasksPayload
import com.yugyd.quiz.domain.courses.model.CourseDetailModel
import com.yugyd.quiz.ui.coursedetails.CourseDetailsView.Action
import com.yugyd.quiz.ui.coursedetails.CourseDetailsView.State
import com.yugyd.quiz.ui.coursedetails.CourseDetailsView.State.CourseDetailsDomainState
import com.yugyd.quiz.ui.coursedetails.CourseDetailsView.State.NavigationState
import com.yugyd.quiz.uikit.LoadingContent
import com.yugyd.quiz.uikit.WarningContent
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.QuizBackground
import com.yugyd.quiz.uikit.component.SimpleToolbar
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme
import com.yugyd.quiz.uikit.R as UiKitR

@Composable
internal fun CourseDetailsRoute(
    viewModel: CourseDetailsViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    onNavigateToAiTasks: (AiTasksPayload) -> Unit,
    onNavigateToExternalReportError: () -> Unit,
    onBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CourseDetailsScreen(
        uiState = state,
        snackbarHostState = snackbarHostState,
        onTasksClicked = {
            viewModel.onAction(Action.OnTasksClicked)
        },
        onReportClicked = {
            viewModel.onAction(Action.OnReportClicked)
        },
        onBackPressed = {
            viewModel.onAction(Action.OnBackPressed)
        },
        onErrorDismissState = {
            viewModel.onAction(Action.OnSnackbarDismissed)
        },
        onBack = onBack,
        onNavigateToAiTasks = onNavigateToAiTasks,
        onNavigateToExternalReportError = onNavigateToExternalReportError,
        onNavigationHandled = {
            viewModel.onAction(Action.OnNavigationHandled)
        },
    )
}

@Composable
internal fun CourseDetailsScreen(
    uiState: State,
    snackbarHostState: SnackbarHostState,
    onTasksClicked: () -> Unit,
    onReportClicked: () -> Unit,
    onErrorDismissState: () -> Unit,
    onBackPressed: () -> Unit,
    onBack: () -> Unit,
    onNavigateToAiTasks: (AiTasksPayload) -> Unit,
    onNavigateToExternalReportError: () -> Unit,
    onNavigationHandled: () -> Unit,
) {
    val errorMessage = stringResource(id = UiKitR.string.error_base)
    LaunchedEffect(key1 = uiState.showErrorMessage) {
        if (uiState.showErrorMessage) {
            snackbarHostState.showSnackbar(message = errorMessage)

            onErrorDismissState()
        }
    }

    Column {
        SimpleToolbar(
            title = uiState.courseTitle,
            onBackPressed = onBackPressed,
            rightIcon = Icons.Default.Report,
            onRightIconClicked = onReportClicked,
        )

        when {
            uiState.isLoading -> {
                LoadingContent()
            }

            uiState.isWarning -> {
                WarningContent()
            }

            else -> {
                CourseDetailsContent(
                    course = uiState.courseDetailsDomainState,
                    isActionsVisible = uiState.isActionsVisible,
                    onTasksClicked = onTasksClicked,
                )
            }
        }
    }

    NavigationHandler(
        navigationState = uiState.navigationState,
        onBack = onBack,
        onNavigateToAiTasks = onNavigateToAiTasks,
        onNavigateToExternalReportError = onNavigateToExternalReportError,
        onNavigationHandled = onNavigationHandled,
    )
}

@Composable
internal fun CourseDetailsContent(
    course: CourseDetailsDomainState,
    isActionsVisible: Boolean,
    onTasksClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 8.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 24.dp,
            )
            .verticalScroll(rememberScrollState()),
    ) {
        Markdown(
            modifier = Modifier.fillMaxWidth(),
            content = course.courseDetailModel?.content.orEmpty(),
        )

        ActionsFooter(
            isActionsVisible = isActionsVisible,
            onTasksClicked = onTasksClicked,
        )
    }
}

@Composable
internal fun ActionsFooter(
    isActionsVisible: Boolean,
    onTasksClicked: () -> Unit,
) {
    if (isActionsVisible) {
        Spacer(
            modifier = Modifier.height(height = 16.dp),
        )

        Box {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onTasksClicked,
            ) {
                Text(text = stringResource(id = R.string.course_details_start_test))
            }
        }
    }
}

@Composable
internal fun NavigationHandler(
    navigationState: NavigationState?,
    onBack: () -> Unit,
    onNavigateToAiTasks: (AiTasksPayload) -> Unit,
    onNavigateToExternalReportError: () -> Unit,
    onNavigationHandled: () -> Unit,
) {
    LaunchedEffect(key1 = navigationState) {
        when (navigationState) {
            is NavigationState.NavigateToTasks -> {
                onNavigateToAiTasks(
                    AiTasksPayload(
                        courseId = navigationState.id,
                        courseTitle = navigationState.title
                    ),
                )
            }

            NavigationState.NavigateToExternalPlatformReportError -> {
                onNavigateToExternalReportError()
            }

            NavigationState.Back -> {
                onBack()
            }

            null -> Unit
        }

        navigationState?.let { onNavigationHandled() }
    }
}

@ThemePreviews
@Composable
private fun ContentPreview(
    @PreviewParameter(CourseDetailsPreviewParameterProvider::class) items: List<CourseDetailModel>,
) {
    QuizApplicationTheme {
        QuizBackground {
            CourseDetailsContent(
                course = CourseDetailsDomainState(courseDetailModel = items.first()),
                isActionsVisible = true,
                onTasksClicked = {},
            )
        }
    }
}
