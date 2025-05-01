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

package com.yugyd.quiz.progressui.progress

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yugyd.quiz.commonui.utils.ProgressUtils
import com.yugyd.quiz.domain.api.payload.SpecificProgressPayload
import com.yugyd.quiz.progressui.R
import com.yugyd.quiz.progressui.model.HeaderProgressUiModel
import com.yugyd.quiz.progressui.model.ItemProgressUiModel
import com.yugyd.quiz.progressui.model.ProgressUiModel
import com.yugyd.quiz.progressui.progress.ProgressView.Action
import com.yugyd.quiz.progressui.progress.ProgressView.State.NavigationState
import com.yugyd.quiz.progressui.shared.ProgressPreviewParameterProvider
import com.yugyd.quiz.uikit.LoadingContent
import com.yugyd.quiz.uikit.WarningContent
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.QuizBackground
import com.yugyd.quiz.uikit.component.RootToolbar
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme
import com.yugyd.quiz.uikit.theme.progressIndicatorHeight
import com.yugyd.quiz.uikit.R as UiKitR

@Composable
internal fun ProgressRoute(
    viewModel: ProgressViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    onNavigateToSpecificProgress: (SpecificProgressPayload) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProgressScreen(
        uiState = state,
        snackbarHostState = snackbarHostState,
        onItemClicked = {
            viewModel.onAction(Action.OnProgressClicked(item = it))
        },
        onErrorDismissState = {
            viewModel.onAction(Action.OnSnackbarDismissed)
        },
        onNavigateToSpecificProgress = onNavigateToSpecificProgress,
        onNavigationHandled = {
            viewModel.onAction(Action.OnNavigationHandled)
        },
    )
}

@Composable
internal fun ProgressScreen(
    uiState: ProgressView.State,
    snackbarHostState: SnackbarHostState,
    onItemClicked: (ItemProgressUiModel) -> Unit,
    onErrorDismissState: () -> Unit,
    onNavigateToSpecificProgress: (SpecificProgressPayload) -> Unit,
    onNavigationHandled: () -> Unit,
) {
    val errorMessage = stringResource(id = UiKitR.string.ds_error_base)
    LaunchedEffect(key1 = uiState.showErrorMessage) {
        if (uiState.showErrorMessage) {
            snackbarHostState.showSnackbar(message = errorMessage)

            onErrorDismissState()
        }
    }

    Column {
        RootToolbar(
            title = stringResource(id = UiKitR.string.ds_navbar_title_progress),
        )

        when {
            uiState.isLoading -> {
                LoadingContent()
            }

            uiState.isWarning -> {
                WarningContent()
            }

            else -> {
                ProgressContent(
                    items = uiState.items,
                    onItemClicked = onItemClicked,
                )
            }
        }
    }

    NavigationHandler(
        navigationState = uiState.navigationState,
        onNavigateToSpecificProgress = onNavigateToSpecificProgress,
        onNavigationHandled = onNavigationHandled,
    )
}

@Composable
internal fun ProgressContent(
    items: List<ProgressUiModel>,
    onItemClicked: ((ItemProgressUiModel) -> Unit)?,
) {
    LazyColumn {
        items(
            items = items,
            key = ProgressUiModel::id
        ) {
            ProgressItem(
                model = it,
                onItemClicked = onItemClicked,
            )
        }
    }
}

@Composable
internal fun ProgressItem(
    model: ProgressUiModel,
    onItemClicked: ((ItemProgressUiModel) -> Unit)?,
) {
    when (model) {
        is HeaderProgressUiModel -> {
            HeaderProgressItem(model = model)
        }

        is ItemProgressUiModel -> {
            ProgressItem(
                model = model,
                onItemClicked = onItemClicked,
            )
        }
    }
}

@Composable
internal fun HeaderProgressItem(
    model: HeaderProgressUiModel,
) {
    Column {
        Surface(
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    painter = painterResource(
                        id = UiKitR.drawable.ic_account_circle
                    ),
                    contentDescription = stringResource(
                        id = R.string.content_description_progress_icon, model.title
                    ),
                    modifier = Modifier.size(size = 96.dp),
                    tint = model.color,
                )

                Spacer(modifier = Modifier.height(height = 16.dp))

                Text(
                    text = model.title,
                    color = model.color,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall,
                )

                Spacer(modifier = Modifier.height(height = 16.dp))

                val animatedProgress by animateFloatAsState(
                    targetValue = ProgressUtils.toFloatPercent(model.progressPercent),
                    animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
                    label = "ProgressFloatAnimation",
                )
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .height(height = progressIndicatorHeight)
                        .defaultMinSize(minWidth = 100.dp),
                    color = model.color,
                )

                Spacer(modifier = Modifier.height(height = 16.dp))

                Text(
                    text = model.progressPercentTitle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }

        HorizontalDivider()
    }
}

@Composable
internal fun ProgressItem(
    model: ItemProgressUiModel,
    onItemClicked: ((ItemProgressUiModel) -> Unit)?,
) {
    ListItem(
        headlineContent = {
            Text(text = model.title)
        },
        modifier = if (onItemClicked != null) {
            Modifier.clickable {
                onItemClicked(model)
            }
        } else {
            Modifier
        },
        supportingContent = {
            Text(text = model.subtitle)
        },
        trailingContent = {
            Text(
                text = model.progressPercentTitle,
                color = model.progressColor,
            )
        },
    )
}

@Composable
internal fun NavigationHandler(
    navigationState: NavigationState?,
    onNavigateToSpecificProgress: (SpecificProgressPayload) -> Unit,
    onNavigationHandled: () -> Unit,
) {
    LaunchedEffect(key1 = navigationState) {
        when (navigationState) {
            is NavigationState.NavigateToSpecificProgress -> {
                onNavigateToSpecificProgress(navigationState.payload)
            }

            null -> Unit
        }

        navigationState?.let { onNavigationHandled() }
    }
}

@ThemePreviews
@Composable
private fun ContentPreview(
    @PreviewParameter(ProgressPreviewParameterProvider::class) items: List<ProgressUiModel>,
) {
    QuizApplicationTheme {
        QuizBackground {
            ProgressContent(
                items = items,
                onItemClicked = {},
            )
        }
    }
}
