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

package com.yugyd.quiz.ui.subcourses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import com.yugyd.quiz.domain.api.payload.CourseDetailsPayload
import com.yugyd.quiz.domain.api.payload.SubCoursePayload
import com.yugyd.quiz.domain.courses.model.CourseModel
import com.yugyd.quiz.ui.commoncourses.ContinueCourseCard
import com.yugyd.quiz.ui.commoncourses.models.ContinueCourseBannerUiModel
import com.yugyd.quiz.ui.subcourses.SubCourseListView.Action
import com.yugyd.quiz.ui.subcourses.SubCourseListView.State
import com.yugyd.quiz.ui.subcourses.SubCourseListView.State.NavigationState
import com.yugyd.quiz.uikit.LoadingContent
import com.yugyd.quiz.uikit.WarningContent
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.QuizBackground
import com.yugyd.quiz.uikit.component.SimpleToolbar
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme
import com.yugyd.quiz.uikit.R as UiKitR

@Composable
internal fun SubCourseListRoute(
    viewModel: SubCourseListViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    onNavigateToSearch: () -> Unit,
    onNavigateToSubCourse: (SubCoursePayload) -> Unit,
    onNavigateToCourseDetails: (CourseDetailsPayload) -> Unit,
    onBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SubCourseListScreen(
        uiState = state,
        snackbarHostState = snackbarHostState,
        onBackPressed = {
            viewModel.onAction(Action.OnBackPressed)
        },
        onSearchClicked = {
            viewModel.onAction(Action.OnSearchClicked)
        },
        onItemClicked = {
            viewModel.onAction(Action.OnCourseClicked(it))
        },
        onBannerConfirmClicked = {
            viewModel.onAction(Action.OnContinueThemeBannerClicked)
        },
        onBannerHideClicked = {
            viewModel.onAction(Action.OnHideThemeBannerClicked)

        },
        onErrorDismissState = {
            viewModel.onAction(Action.OnSnackbarDismissed)
        },
        onBack = onBack,
        onNavigateToSearch = onNavigateToSearch,
        onNavigateToCourse = onNavigateToSubCourse,
        onNavigateToCourseDetails = onNavigateToCourseDetails,
        onNavigationHandled = {
            viewModel.onAction(Action.OnNavigationHandled)
        },
    )
}

@Composable
internal fun SubCourseListScreen(
    uiState: State,
    snackbarHostState: SnackbarHostState,
    onSearchClicked: () -> Unit,
    onItemClicked: (CourseModel) -> Unit,
    onBannerConfirmClicked: () -> Unit,
    onBannerHideClicked: () -> Unit,
    onErrorDismissState: () -> Unit,
    onBackPressed: () -> Unit,
    onBack: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToCourse: (SubCoursePayload) -> Unit,
    onNavigateToCourseDetails: (CourseDetailsPayload) -> Unit,
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
            title = uiState.parentCourseTitle,
            onBackPressed = onBackPressed,
            rightIcon = Icons.Default.Search,
            onRightIconClicked = onSearchClicked,
        )

        when {
            uiState.isLoading -> {
                LoadingContent()
            }

            uiState.isWarning -> {
                WarningContent()
            }

            else -> {
                SubCourseListContent(
                    items = uiState.items,
                    courseBanner = uiState.continueCourseBanner,
                    onItemClicked = onItemClicked,
                    onBannerConfirmClicked = onBannerConfirmClicked,
                    onBannerHideClicked = onBannerHideClicked,
                )
            }
        }
    }

    NavigationHandler(
        navigationState = uiState.navigationState,
        onBack = onBack,
        onNavigateToSearch = onNavigateToSearch,
        onNavigateToCourse = onNavigateToCourse,
        onNavigateToCourseDetails = onNavigateToCourseDetails,
        onNavigationHandled = onNavigationHandled,
    )
}

@Composable
internal fun SubCourseListContent(
    courseBanner: ContinueCourseBannerUiModel?,
    items: List<CourseModel>,
    onItemClicked: (CourseModel) -> Unit,
    onBannerConfirmClicked: () -> Unit,
    onBannerHideClicked: () -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp),
    ) {
        if (courseBanner != null) {
            item(
                key = "header",
                contentType = "continue_course_banner",
            ) {
                ContinueCourseCard(
                    courseBanner = courseBanner,
                    onConfirmClicked = onBannerConfirmClicked,
                    onHideClicked = onBannerHideClicked,
                )
            }
        }

        items(
            items = items,
            key = CourseModel::id,
            contentType = {
                "course_item"
            },
        ) {
            CourseItem(
                model = it,
                onItemClicked = onItemClicked,
            )
        }
    }
}

@Composable
internal fun CourseItem(
    model: CourseModel,
    onItemClicked: (CourseModel) -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
    ) {
        ListItem(
            modifier = Modifier
                .clickable {
                    onItemClicked(model)
                }
                .fillMaxWidth(),
            headlineContent = {
                Text(text = model.name)
            },
        )
    }
}

@Composable
internal fun NavigationHandler(
    navigationState: NavigationState?,
    onBack: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToCourse: (SubCoursePayload) -> Unit,
    onNavigateToCourseDetails: (CourseDetailsPayload) -> Unit,
    onNavigationHandled: () -> Unit,
) {
    LaunchedEffect(key1 = navigationState) {
        when (navigationState) {
            is NavigationState.NavigateToSearch -> {
                onNavigateToSearch()
            }

            is NavigationState.NavigateToSubCourse -> {
                onNavigateToCourse(
                    SubCoursePayload(
                        courseId = navigationState.id,
                        courseTitle = navigationState.title,
                        isHideContinueBanner = navigationState.isHideContinueBanner,
                    ),
                )
            }

            is NavigationState.NavigateToCourseDetail -> {
                onNavigateToCourseDetails(
                    CourseDetailsPayload(
                        courseId = navigationState.id,
                        courseTitle = navigationState.title
                    ),
                )
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
    @PreviewParameter(SubCourseListPreviewParameterProvider::class) items: List<CourseModel>,
) {
    QuizApplicationTheme {
        QuizBackground {
            SubCourseListContent(
                items = items,
                courseBanner = ContinueCourseBannerUiModel(
                    id = 1,
                    title = "Title",
                ),
                onItemClicked = {},
                onBannerConfirmClicked = {},
                onBannerHideClicked = {},
            )
        }
    }
}
