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

package com.yugyd.quiz.ui.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yugyd.quiz.domain.api.model.payload.GamePayload
import com.yugyd.quiz.domain.section.model.ProgressState
import com.yugyd.quiz.domain.section.model.Section
import com.yugyd.quiz.ui.section.SectionView.Action
import com.yugyd.quiz.ui.section.SectionView.State
import com.yugyd.quiz.ui.section.SectionView.State.NavigationState
import com.yugyd.quiz.uikit.LoadingContent
import com.yugyd.quiz.uikit.WarningContent
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.QuizBackground
import com.yugyd.quiz.uikit.component.SimpleToolbar
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme
import com.yugyd.quiz.uikit.theme.app_color_negative
import com.yugyd.quiz.uikit.theme.app_color_neutral
import com.yugyd.quiz.uikit.theme.app_color_positive
import com.yugyd.quiz.uikit.R as UiKitR

@Composable
internal fun SectionRoute(
    viewModel: SectionViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    onNavigateToGame: (GamePayload) -> Unit,
    onBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SectionScreen(
        uiState = state,
        snackbarHostState = snackbarHostState,
        onBackPressed = {
            viewModel.onAction(Action.OnBackPressed)
        },
        onItemClicked = {
            viewModel.onAction(Action.OnSectionClicked(it))
        },
        onErrorDismissState = {
            viewModel.onAction(Action.OnSnackbarDismissed)
        },
        onNavigateToGame = onNavigateToGame,
        onBack = onBack,
        onNavigationHandled = {
            viewModel.onAction(Action.OnNavigationHandled)
        },
    )
}

@Composable
internal fun SectionScreen(
    uiState: State,
    snackbarHostState: SnackbarHostState,
    onBackPressed: () -> Unit,
    onItemClicked: (Section) -> Unit,
    onErrorDismissState: () -> Unit,
    onNavigateToGame: (GamePayload) -> Unit,
    onBack: () -> Unit,
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
            title = uiState.themeTitle,
            onBackPressed = onBackPressed,
        )

        when {
            uiState.isLoading -> {
                LoadingContent()
            }

            uiState.isWarning -> {
                WarningContent()
            }

            else -> {
                SectionContent(
                    items = uiState.models,
                    onItemClicked = onItemClicked,
                )
            }
        }
    }

    NavigationHandler(
        navigationState = uiState.navigationState,
        onNavigateToGame = onNavigateToGame,
        onBack = onBack,
        onNavigationHandled = onNavigationHandled,
    )
}

internal const val PHONE_SPAN_COUNT = 3
internal const val TABLET_SPAN_COUNT = 4

@Composable
internal fun SectionContent(
    items: List<Section>,
    onItemClicked: (Section) -> Unit,
) {
    val isTablet = booleanResource(id = UiKitR.bool.isTablet)
    val spanCount = if (isTablet) {
        TABLET_SPAN_COUNT
    } else {
        PHONE_SPAN_COUNT
    }

    val itemPadding = 8.dp
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = spanCount),
        contentPadding = PaddingValues(all = itemPadding),
        verticalArrangement = Arrangement.spacedBy(space = itemPadding),
        horizontalArrangement = Arrangement.spacedBy(space = itemPadding)
    ) {
        items(
            items = items,
            key = Section::id
        ) {
            SectionItem(
                model = it,
                onItemClicked = onItemClicked
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SectionItem(
    model: Section,
    onItemClicked: (Section) -> Unit,
) {
    Card(
        modifier = Modifier.aspectRatio(1F),
        onClick = { onItemClicked(model) }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2F),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = model.id.toString(),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium,
                )
            }

            val progressColor = when (model.progressState) {
                ProgressState.LOCKED -> MaterialTheme.colorScheme.onSurfaceVariant
                ProgressState.EMPTY -> MaterialTheme.colorScheme.onSurface
                ProgressState.LOW -> app_color_negative
                ProgressState.MEDIUM -> app_color_neutral
                ProgressState.SUCCESS -> app_color_positive
            }
            Surface(
                color = progressColor.copy(alpha = 0.2F),
                modifier = Modifier.weight(1F),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    val progressIcon = when (model.progressState) {
                        ProgressState.LOCKED -> painterResource(
                            id = R.drawable.ic_lock
                        )

                        ProgressState.EMPTY -> painterResource(
                            id = R.drawable.ic_radio_button_unchecked
                        )

                        ProgressState.LOW -> painterResource(id = R.drawable.ic_radio_button_checked)
                        ProgressState.MEDIUM -> painterResource(
                            id = R.drawable.ic_radio_button_checked
                        )

                        ProgressState.SUCCESS -> painterResource(
                            id = R.drawable.ic_radio_button_checked
                        )
                    }
                    Icon(
                        painter = progressIcon,
                        contentDescription = null,
                        tint = progressColor
                    )
                }
            }
        }
    }
}

@Composable
internal fun NavigationHandler(
    navigationState: NavigationState?,
    onNavigateToGame: (GamePayload) -> Unit,
    onBack: () -> Unit,
    onNavigationHandled: () -> Unit,
) {
    LaunchedEffect(key1 = navigationState) {
        when (navigationState) {
            NavigationState.Back -> onBack()
            is NavigationState.NavigateToGame -> onNavigateToGame(navigationState.payload)
            null -> Unit
        }

        navigationState?.let { onNavigationHandled() }
    }
}

@ThemePreviews
@Composable
private fun ContentPreview(
    @PreviewParameter(SectionsPreviewParameterProvider::class) items: List<Section>,
) {
    QuizApplicationTheme {
        QuizBackground {
            SectionContent(
                items = items,
                onItemClicked = {},
            )
        }
    }
}
