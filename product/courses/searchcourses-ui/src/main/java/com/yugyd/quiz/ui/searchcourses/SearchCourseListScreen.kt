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

package com.yugyd.quiz.ui.searchcourses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yugyd.quiz.domain.api.payload.CourseDetailsPayload
import com.yugyd.quiz.domain.api.payload.SubCoursePayload
import com.yugyd.quiz.domain.courses.model.CourseModel
import com.yugyd.quiz.ui.searchcourses.SearchCourseView.Action
import com.yugyd.quiz.ui.searchcourses.SearchCourseView.State
import com.yugyd.quiz.ui.searchcourses.SearchCourseView.State.NavigationState
import com.yugyd.quiz.uikit.LoadingContent
import com.yugyd.quiz.uikit.WarningContent
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.QuizBackground
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme
import com.yugyd.quiz.uikit.R as UiKitR

@Composable
internal fun SearchCourseRoute(
    viewModel: SearchCourseViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    onNavigateToSubCourse: (SubCoursePayload) -> Unit,
    onNavigateToCourseDetails: (CourseDetailsPayload) -> Unit,
    onBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SearchCourseScreen(
        uiState = state,
        snackbarHostState = snackbarHostState,
        onBackPressed = {
            viewModel.onAction(Action.OnBackPressed)
        },
        onItemClicked = {
            viewModel.onAction(Action.OnCourseClicked(it))
        },
        onSearchQueryChanged = {
            viewModel.onAction(Action.OnSearchQueryChanged(it))
        },
        onSearchClicked = {
            viewModel.onAction(Action.OnSearchClicked(it))
        },
        onErrorDismissState = {
            viewModel.onAction(Action.OnSnackbarDismissed)
        },
        onBack = onBack,
        onNavigateToCourse = onNavigateToSubCourse,
        onNavigateToCourseDetails = onNavigateToCourseDetails,
        onNavigationHandled = {
            viewModel.onAction(Action.OnNavigationHandled)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchCourseScreen(
    uiState: State,
    snackbarHostState: SnackbarHostState,
    onItemClicked: (CourseModel) -> Unit,
    onSearchClicked: (String?) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onErrorDismissState: () -> Unit,
    onBackPressed: () -> Unit,
    onBack: () -> Unit,
    onNavigateToCourse: (SubCoursePayload) -> Unit,
    onNavigateToCourseDetails: (CourseDetailsPayload) -> Unit,
    onNavigationHandled: () -> Unit,
) {
    val errorMessage = stringResource(id = UiKitR.string.ds_error_base)
    LaunchedEffect(key1 = uiState.showErrorMessage) {
        if (uiState.showErrorMessage) {
            snackbarHostState.showSnackbar(message = errorMessage)

            onErrorDismissState()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true },
    ) {
        SearchBar(
            modifier = Modifier.semantics { traversalIndex = 0F },
            inputField = {
                SearchBarDefaults.InputField(
                    query = uiState.searchState.query,
                    onQueryChange = onSearchQueryChanged,
                    onSearch = { onSearchClicked(null) },
                    expanded = true,
                    onExpandedChange = {},
                    placeholder = {
                        Text(text = stringResource(id = R.string.search_course_title_search_theme))
                    },
                    leadingIcon = {
                        IconButton(
                            onClick = onBackPressed,
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                            )
                        }
                    },
                    enabled = uiState.searchState.isEnabled,
                )
            },
            // Only search view state
            expanded = true,
            onExpandedChange = {},
        ) {
            Column {
                when {
                    uiState.isLoading -> {
                        LoadingContent()
                    }

                    uiState.isWarning -> {
                        WarningContent()
                    }

                    uiState.searchState.currentRequestQuery.isNotBlank() && uiState.items.isEmpty() -> {
                        WarningContent(
                            icon = UiKitR.drawable.ic_search,
                            message = stringResource(
                                id = R.string.search_course_empty_state_message,
                                uiState.searchState.currentRequestQuery,
                            )
                        )
                    }

                    else -> {
                        SearchCourseContent(
                            items = uiState.items,
                            suggestions = uiState.searchState.suggestions,
                            onItemClicked = onItemClicked,
                            onSearchClicked = onSearchClicked,
                        )
                    }
                }
            }
        }
    }

    NavigationHandler(
        navigationState = uiState.navigationState,
        onBack = onBack,
        onNavigateToCourse = onNavigateToCourse,
        onNavigateToCourseDetails = onNavigateToCourseDetails,
        onNavigationHandled = onNavigationHandled,
    )
}

private enum class SearchCourseItemType {
    SUGGEST_ITEM, COURSE_ITEM, HEADER_ITEM,
}

@Composable
internal fun SearchCourseContent(
    suggestions: List<String>,
    items: List<CourseModel>,
    onItemClicked: (CourseModel) -> Unit,
    onSearchClicked: (String) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp),
    ) {
        items(
            items = suggestions,
            key = { it },
            contentType = {
                SearchCourseItemType.SUGGEST_ITEM
            },
        ) { suggest ->
            ListItem(
                headlineContent = { Text(suggest) },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                modifier = Modifier
                    .clickable {
                        onSearchClicked(suggest)
                    }
                    .fillMaxWidth(),
                leadingContent = {
                    Icon(
                        painter = painterResource(id = UiKitR.drawable.ic_history),
                        contentDescription = null,
                    )
                }
            )
        }

        if (items.isNotEmpty()) {
            item(
                key = "theme_section",
                contentType = SearchCourseItemType.HEADER_ITEM,
            ) {
                ListItem(
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                    headlineContent = {
                        Text(
                            text = stringResource(id = R.string.search_course_title_themes),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                )
            }
        }

        items(
            items = items,
            key = CourseModel::id,
            contentType = {
                SearchCourseItemType.COURSE_ITEM
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
fun CourseItem(
    model: CourseModel,
    onItemClicked: (CourseModel) -> Unit,
) {
    ListItem(
        modifier = Modifier
            .clickable {
                onItemClicked(model)
            }
            .fillMaxWidth(),
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        headlineContent = {
            Text(text = model.name)
        },
    )
}

@Composable
internal fun NavigationHandler(
    navigationState: NavigationState?,
    onBack: () -> Unit,
    onNavigateToCourse: (SubCoursePayload) -> Unit,
    onNavigateToCourseDetails: (CourseDetailsPayload) -> Unit,
    onNavigationHandled: () -> Unit,
) {
    LaunchedEffect(key1 = navigationState) {
        when (navigationState) {

            is NavigationState.NavigateToSubCourse -> {
                onBack()
                onNavigateToCourse(
                    SubCoursePayload(
                        courseId = navigationState.id,
                        courseTitle = navigationState.title,
                        isHideContinueBanner = navigationState.isHideContinueCourseBanner,
                    ),
                )
            }

            is NavigationState.NavigateToCourseDetail -> {
                onBack()
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
    @PreviewParameter(SearchCoursePreviewParameterProvider::class) items: List<CourseModel>,
) {
    QuizApplicationTheme {
        QuizBackground {
            SearchCourseContent(
                items = items,
                onItemClicked = {},
                onSearchClicked = {},
                suggestions = listOf("Foo", "Bar"),
            )
        }
    }
}
