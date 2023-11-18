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

package ${packageName}

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ${domainPackage}.model.${modelName}
import ${packageName}.${viewName}.Action
import ${packageName}.${viewName}.State.NavigationState
import ${packageName}.R
import ${uiKitPackage}.LoadingContent
import ${uiKitPackage}.WarningContent
import ${uiKitPackage}.common.ThemePreviews
import ${uiKitPackage}.component.QuizBackground
import ${uiKitPackage}.component.SimpleToolbar
import ${uiKitPackage}.theme.QuizApplicationTheme
import ${uiKitPackage}.R as UiKitR

@Composable
fun ${routeName}(
    viewModel: ${viewModelName} = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onNavigateToBrowser: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ${screenName}(
        uiState = state,
        snackbarHostState = snackbarHostState,
        onBackPressed = {
            viewModel.onAction(Action.OnBackClicked)
        },
        onItemClicked = {
            viewModel.onAction(Action.OnItemClicked(it))
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
internal fun ${screenName}(
    uiState: ${viewName}.State,
    snackbarHostState: SnackbarHostState,
    onBackPressed: () -> Unit,
    onItemClicked: (${modelName}) -> Unit,
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

    Column {
        SimpleToolbar(
            title = stringResource(id = R.string.${titleName}),
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
                ${contentName}(
                    item = uiState.item,
                    onItemClicked = onItemClicked,
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
internal fun ${contentName}(
    item: ${modelName},
    onItemClicked: (${modelName}) -> Unit,
) {}

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
    @PreviewParameter(${previewParameterProviderName}::class) item: ${modelName},
) {
    QuizApplicationTheme {
        QuizBackground {
            ${contentName}(
                item = item,
                onItemClicked = {},
            )
        }
    }
}
