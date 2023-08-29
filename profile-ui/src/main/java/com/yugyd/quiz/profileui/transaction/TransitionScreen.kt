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

package com.yugyd.quiz.profileui.transaction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yugyd.quiz.profileui.R
import com.yugyd.quiz.profileui.transaction.TransitionView.Action
import com.yugyd.quiz.profileui.transaction.TransitionView.State.NavigationState
import com.yugyd.quiz.profileui.transaction.model.TransitionUiModel
import com.yugyd.quiz.uikit.LoadingContent
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.QuizBackground
import com.yugyd.quiz.uikit.component.SimpleToolbar
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme

@Composable
fun TransitionRoute(
    viewModel: TransitionViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TransitionScreen(
        uiState = state,
        onBackPressed = {
            viewModel.onAction(Action.OnBackPressed)
        },
        onItemClicked = {
            viewModel.onAction(Action.OnTransitionClicked(it))
        },
        onBack = onBack,
        onNavigationHandled = {
            viewModel.onAction(Action.OnNavigationHandled)
        },
    )
}

@Composable
internal fun TransitionScreen(
    uiState: TransitionView.State,
    onBackPressed: () -> Unit,
    onItemClicked: (TransitionUiModel) -> Unit,
    onBack: () -> Unit,
    onNavigationHandled: () -> Unit,
) {
    Column {
        SimpleToolbar(
            title = stringResource(id = R.string.title_show_answer),
            onBackPressed = onBackPressed,
        )

        when {
            uiState.isLoading -> {
                LoadingContent()
            }

            else -> {
                TransitionContent(
                    items = uiState.items,
                    onItemClicked = onItemClicked,
                )
            }
        }
    }

    NavigationHandler(
        navigationState = uiState.navigationState,
        onBack = onBack,
        onNavigationHandled = onNavigationHandled,
    )
}

@Composable
internal fun TransitionContent(
    items: List<TransitionUiModel>,
    onItemClicked: (TransitionUiModel) -> Unit,
) {
    LazyColumn {
        items(
            items = items,
            key = TransitionUiModel::id
        ) { transition ->
            TransitionItem(
                transition = transition,
                onItemClicked = onItemClicked,
            )
        }
    }
}

@Composable
internal fun TransitionItem(
    transition: TransitionUiModel,
    onItemClicked: (TransitionUiModel) -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable {
                onItemClicked(transition)
            }
            .defaultMinSize(minHeight = 48.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            modifier = Modifier.weight(1F),
            text = transition.title,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.width(16.dp))

        val alpha = if (transition.isChecked) {
            1.0F
        } else {
            0.0F
        }
        Icon(
            modifier = Modifier.alpha(alpha),
            painter = painterResource(id = R.drawable.ic_check),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
internal fun NavigationHandler(
    navigationState: NavigationState?,
    onBack: () -> Unit,
    onNavigationHandled: () -> Unit,
) {
    LaunchedEffect(key1 = navigationState) {
        when (navigationState) {
            NavigationState.Back -> onBack()
            null -> Unit
        }

        navigationState?.let { onNavigationHandled() }
    }
}

@ThemePreviews
@Composable
private fun ContentPreview(
    @PreviewParameter(TransitionPreviewParameterProvider::class) items: List<TransitionUiModel>,
) {
    QuizApplicationTheme {
        QuizBackground {
            TransitionContent(
                items = items,
                onItemClicked = {},
            )
        }
    }
}
