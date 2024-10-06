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

package com.yugyd.quiz.ui.tasks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Badge
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yugyd.quiz.domain.tasks.model.TaskModel
import com.yugyd.quiz.ui.favorites.FavoriteIcon
import com.yugyd.quiz.ui.tasks.TasksView.Action
import com.yugyd.quiz.ui.tasks.TasksView.State.FilterUiModel
import com.yugyd.quiz.ui.tasks.TasksView.State.NavigationState
import com.yugyd.quiz.uikit.LoadingContent
import com.yugyd.quiz.uikit.WarningContent
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.QuizBackground
import com.yugyd.quiz.uikit.component.SimpleToolbar
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme
import com.yugyd.quiz.uikit.R as UiKitR

@Composable
internal fun TasksRoute(
    viewModel: TasksViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onNavigateToBrowser: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TasksScreen(
        uiState = state,
        snackbarHostState = snackbarHostState,
        onBackPressed = {
            viewModel.onAction(Action.OnBackClicked)
        },
        onShowFilterClicked = {
            viewModel.onAction(Action.OnShowFilterClicked)
        },
        onItemClicked = {
            viewModel.onAction(Action.OnTaskClicked(it))
        },
        onFavoriteClicked = {
            viewModel.onAction(Action.OnFavoriteClicked(it))
        },
        onFilterClicked = {
            viewModel.onAction(Action.OnFilterClicked(it))
        },
        onFiltersDismissState = {
            viewModel.onAction(Action.OnFiltersDismissed)
        },
        onErrorDismissState = {
            viewModel.onAction(Action.OnSnackbarDismissed)
        },
        onBack = onBack,
        onNavigateToBrowser = onNavigateToBrowser,
        onNavigationHandled = {
            viewModel.onAction(Action.OnNavigationHandled)
        },
    )
}

@Composable
internal fun TasksScreen(
    uiState: TasksView.State,
    snackbarHostState: SnackbarHostState,
    onBackPressed: () -> Unit,
    onShowFilterClicked: () -> Unit,
    onItemClicked: (TaskModel) -> Unit,
    onFavoriteClicked: (TaskModel) -> Unit,
    onFilterClicked: (FilterUiModel) -> Unit,
    onFiltersDismissState: () -> Unit,
    onErrorDismissState: () -> Unit,
    onBack: () -> Unit,
    onNavigateToBrowser: (String) -> Unit,
    onNavigationHandled: () -> Unit,
) {
    val errorMessage = stringResource(id = UiKitR.string.error_base)
    LaunchedEffect(key1 = uiState.showErrorMessage) {
        if (uiState.showErrorMessage) {
            snackbarHostState.showSnackbar(message = errorMessage)

            onErrorDismissState()
        }
    }

    TasksFiltersBottomSheet(
        openBottomSheet = uiState.showFilters,
        filters = uiState.allFilters,
        onFilterClicked = onFilterClicked,
        onDismissRequest = onFiltersDismissState,
    )

    Column {
        SimpleToolbar(
            title = stringResource(id = R.string.tasks_title_error_list),
            onBackPressed = onBackPressed,
            rightIcon = Icons.Filled.Tune,
            onRightIconClicked = onShowFilterClicked,
        )

        when {
            uiState.isLoading -> {
                LoadingContent()
            }

            uiState.isWarning -> {
                WarningContent()
            }

            else -> {
                val showedItems = remember(uiState.allItems) {
                    uiState.allItems.filter(TaskModel::isShow)
                }

                TasksContent(
                    items = showedItems,
                    onItemClicked = onItemClicked,
                    onFavoriteClicked = onFavoriteClicked,
                )
            }
        }
    }

    NavigationHandler(
        navigationState = uiState.navigationState,
        onBack = onBack,
        onNavigateToBrowser = onNavigateToBrowser,
        onNavigationHandled = onNavigationHandled,
    )
}

@Composable
internal fun TasksContent(
    items: List<TaskModel>,
    onItemClicked: (TaskModel) -> Unit,
    onFavoriteClicked: (TaskModel) -> Unit,
) {
    LazyColumn {
        items(
            items = items, key = TaskModel::id
        ) {
            TaskItem(
                model = it,
                onItemClicked = onItemClicked,
                onFavoriteClicked = onFavoriteClicked,
            )
        }
    }
}

@Composable
internal fun TaskItem(
    model: TaskModel,
    onItemClicked: (TaskModel) -> Unit,
    onFavoriteClicked: (TaskModel) -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    onItemClicked(model)
                }
                .fillMaxWidth()
                .padding(all = 16.dp),
        ) {
            Column(
                modifier = Modifier.weight(weight = 1F),
            ) {
                Text(
                    text = model.quest,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = model.trueAnswer,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Badge(
                    modifier = Modifier.defaultMinSize(minWidth = 24.dp),
                ) {
                    Text(text = model.complexity.toString())
                }

            }

            Spacer(modifier = Modifier.width(width = 8.dp))

            FavoriteIcon(
                isFavorite = model.isFavorite,
                onFavoriteClicked = {
                    onFavoriteClicked(model)
                },
            )
        }
    }

    Divider()
}

@Composable
internal fun NavigationHandler(
    navigationState: NavigationState?,
    onBack: () -> Unit,
    onNavigateToBrowser: (String) -> Unit,
    onNavigationHandled: () -> Unit,
) {
    LaunchedEffect(key1 = navigationState) {
        when (navigationState) {
            NavigationState.Back -> onBack()
            is NavigationState.NavigateToExternalBrowser -> onNavigateToBrowser(navigationState.url)
            null -> Unit
        }

        navigationState?.let { onNavigationHandled() }
    }
}

@ThemePreviews
@Composable
private fun ContentPreview(
    @PreviewParameter(TasksPreviewParameterProvider::class) items: List<TaskModel>,
) {
    QuizApplicationTheme {
        QuizBackground {
            TasksContent(
                items = items,
                onItemClicked = {},
                onFavoriteClicked = {},
            )
        }
    }
}
