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

package com.yugyd.quiz.proui.proonboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yugyd.quiz.proui.R
import com.yugyd.quiz.proui.proonboarding.ProOnboardingView.Action
import com.yugyd.quiz.proui.proonboarding.ProOnboardingView.State.NavigationState
import com.yugyd.quiz.proui.proonboarding.model.ProOnboardingUiModel
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.QuizBackground
import com.yugyd.quiz.uikit.component.defaultToolbarHeight
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme

@Composable
fun ProOnboardingRoute(
    viewModel: ProOnboardingViewModel = hiltViewModel(),
    onNavigateToPro: () -> Unit,
    onBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProOnboardingScreen(
        uiState = state,
        onBackPressed = {
            viewModel.onAction(Action.OnBackClicked)
        },
        onContinueClicked = {
            viewModel.onAction(Action.OnContinueClicked)
        },
        onNavigateToPro = onNavigateToPro,
        onBack = onBack,
        onNavigationHandled = {
            viewModel.onAction(Action.OnNavigationHandled)
        },
    )
}

@Composable
internal fun ProOnboardingScreen(
    uiState: ProOnboardingView.State,
    onBackPressed: () -> Unit,
    onContinueClicked: () -> Unit,
    onNavigateToPro: () -> Unit,
    onBack: () -> Unit,
    onNavigationHandled: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CloseToolbar(onBackPressed = onBackPressed)

        PagerContent(
            modifier = Modifier.weight(weight = 1F),
            items = uiState.items,
        )

        Box(
            modifier = Modifier.padding(all = 16.dp)
        ) {
            Button(
                onClick = onContinueClicked,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(id = R.string.action_attempt)
                )
            }
        }
    }

    NavigationHandler(
        navigationState = uiState.navigationState,
        onNavigateToPro = onNavigateToPro,
        onBack = onBack,
        onNavigationHandled = onNavigationHandled,
    )
}

@Composable
internal fun CloseToolbar(
    onBackPressed: () -> Unit,
) {
    Box(
        modifier = Modifier
            .defaultMinSize(minHeight = defaultToolbarHeight)
            .fillMaxWidth()
            .padding(
                horizontal = 4.dp,
                vertical = 8.dp,
            ),
    ) {
        IconButton(onClick = onBackPressed) {
            Icon(
                painter = painterResource(id = R.drawable.ic_close_48),
                contentDescription = null,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun PagerContent(
    modifier: Modifier,
    items: List<ProOnboardingUiModel>,
) {
    Box(
        modifier = modifier.padding(vertical = 16.dp)
    ) {
        val count = items.count()
        val pagerState = rememberPagerState()
        HorizontalPager(
            modifier = modifier,
            state = pagerState,
            pageCount = count,
            contentPadding = PaddingValues(all = 16.dp),
        ) { page ->
            ProOnboardingItem(model = items[page])
        }

        Row(
            Modifier
                .height(height = 12.dp)
                .fillMaxWidth()
                .align(alignment = Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(count) { iteration ->
                val color = if (pagerState.currentPage == iteration) {
                    Color.DarkGray
                } else {
                    Color.LightGray
                }
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }
    }
}

@Composable
internal fun NavigationHandler(
    navigationState: NavigationState?,
    onNavigateToPro: () -> Unit,
    onBack: () -> Unit,
    onNavigationHandled: () -> Unit,
) {
    LaunchedEffect(key1 = navigationState) {
        when (navigationState) {
            NavigationState.Back -> onBack()
            NavigationState.NavigateToPro -> onNavigateToPro()
            null -> Unit
        }

        navigationState?.let { onNavigationHandled() }
    }
}

@ThemePreviews
@Composable
private fun ContentPreview(
    @PreviewParameter(ProOnboardingPreviewParameterProvider::class) items: List<ProOnboardingUiModel>,
) {
    QuizApplicationTheme {
        QuizBackground {
            ProOnboardingScreen(
                uiState = ProOnboardingView.State(items = items),
                onBackPressed = {},
                onContinueClicked = {},
                onNavigateToPro = {},
                onBack = {},
                onNavigationHandled = {},
            )
        }
    }
}
