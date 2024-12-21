/*
 * Copyright 2024 Roman Likhachev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yugyd.quiz.ui.aisettings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yugyd.quiz.domain.api.payload.AiConnectionDetailsPayload
import com.yugyd.quiz.ui.aisettings.AiSettingsView.Action
import com.yugyd.quiz.ui.aisettings.AiSettingsView.State
import com.yugyd.quiz.ui.aisettings.AiSettingsView.State.NavigationState
import com.yugyd.quiz.ui.aisettings.model.AiConnectionUiModel
import com.yugyd.quiz.uikit.LoadingContent
import com.yugyd.quiz.uikit.WarningContent
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.QuizBackground
import com.yugyd.quiz.uikit.component.SimpleToolbar
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme
import com.yugyd.quiz.uikit.R as UiKitR

@Composable
internal fun AiSettingsRoute(
    viewModel: AiSettingsViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onNavigateToAiConnectionDetails: (AiConnectionDetailsPayload) -> Unit,
    onNavigateToBrowser: (String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AiSettingsScreen(
        uiState = state,
        snackbarHostState = snackbarHostState,
        onInfoClicked = {
            viewModel.onAction(Action.OnInfoClicked)
        },
        onAiEnabledChecked = {
            viewModel.onAction(Action.OnAiEnabledChecked(it))
        },
        onAiConnectionClicked = {
            viewModel.onAction(Action.OnAiConnectionClicked(it))
        },
        onEditAiConnectionClicked = {
            viewModel.onAction(Action.OnEditAiConnectionClicked(it))
        },
        onAddAiConnectionClicked = {
            viewModel.onAction(Action.OnAddAiConnectionClicked)
        },
        onUpdateTermClicked = {
            viewModel.onAction(Action.OnUpdateTermClicked)
        },
        onDirectAiConnectionChecked = {
            viewModel.onAction(Action.OnDirectAiConnectionChecked(it))
        },
        onBackClicked = {
            viewModel.onAction(Action.OnBackClicked)
        },
        onInfoDialogDismissState = {
            viewModel.onAction(Action.OnInfoDialogDismissed)
        },
        onNavigationHandled = {
            viewModel.onAction(Action.OnNavigationHandled)
        },
        onErrorDismissState = {
            viewModel.onAction(Action.OnSnackbarDismissed)
        },
        onNavigateToAiConnectionDetails = onNavigateToAiConnectionDetails,
        onBack = onBack,
        onNavigateToBrowser = onNavigateToBrowser,
        onAiTermOfCacheDialogDismissState = {
            viewModel.onAction(Action.OnTermOfCacheDialogDismissed)
        },
        onAiTermOfCacheSaveClicked = {
            viewModel.onAction(Action.OnTermOfCacheSaveClicked)
        },
        onAiTermOfCacheOptionSelected = {
            viewModel.onAction(Action.OnTermOfCacheOptionSelected(it))
        },
        onAiUsagePolicyClicked = {
            viewModel.onAction(Action.OnAiUsagePolicyClicked)
        },
    )
}

@Composable
internal fun AiSettingsScreen(
    uiState: State,
    snackbarHostState: SnackbarHostState,
    onInfoClicked: () -> Unit,
    onAiEnabledChecked: (Boolean) -> Unit,
    onAiConnectionClicked: (AiConnectionUiModel) -> Unit,
    onEditAiConnectionClicked: (AiConnectionUiModel) -> Unit,
    onAddAiConnectionClicked: () -> Unit,
    onUpdateTermClicked: () -> Unit,
    onDirectAiConnectionChecked: (Boolean) -> Unit,
    onBackClicked: () -> Unit,
    onBack: () -> Unit,
    onNavigateToAiConnectionDetails: (AiConnectionDetailsPayload) -> Unit,
    onInfoDialogDismissState: () -> Unit,
    onErrorDismissState: () -> Unit,
    onNavigationHandled: () -> Unit,
    onAiTermOfCacheSaveClicked: () -> Unit,
    onAiTermOfCacheOptionSelected: (String) -> Unit,
    onAiTermOfCacheDialogDismissState: () -> Unit,
    onAiUsagePolicyClicked: () -> Unit,
    onNavigateToBrowser: (String) -> Unit,
) {
    val errorMessage = stringResource(id = UiKitR.string.error_base)
    LaunchedEffect(key1 = uiState.showErrorMessage) {
        if (uiState.showErrorMessage) {
            snackbarHostState.showSnackbar(message = errorMessage)

            onErrorDismissState()
        }
    }

    if (uiState.isInfoDialogVisible) {
        AiInfoDialog(onInfoDialogDismissState = onInfoDialogDismissState)
    }

    if (uiState.isTermOfCacheDialogVisible && uiState.newTermOfCacheValue != null) {
        val options = remember(uiState.items) {
            uiState.termOfCacheOptions.map { it.toString() }
        }
        val selectedOption = remember(uiState.newTermOfCacheValue) {
            uiState.newTermOfCacheValue.toString()

        }
        AiTermOfCacheDialog(
            options = options,
            selectedOption = selectedOption,
            onDismissRequest = onAiTermOfCacheDialogDismissState,
            onConfirmation = onAiTermOfCacheSaveClicked,
            onOptionSelected = onAiTermOfCacheOptionSelected,
        )
    }

    Column {
        SimpleToolbar(
            title = stringResource(id = R.string.ai_settings_title_ai),
            onBackPressed = onBackClicked,
            onRightIconClicked = onInfoClicked,
            rightIcon = Icons.Default.Info,
        )

        when {
            uiState.isLoading -> {
                LoadingContent()
            }

            uiState.isWarning -> {
                WarningContent()
            }

            else -> {
                AiSettingsContent(
                    items = uiState.items,
                    isAiEnabled = uiState.isAiEnabled,
                    termOfCacheValue = uiState.termOfCacheValue,
                    isDirectConnectionEnabled = uiState.isDirectConnectionEnabled,
                    isDirectConnectionVisible = false,
                    isAddConnectionVisible = uiState.isAddConnectionVisible,
                    onAiEnabledChecked = onAiEnabledChecked,
                    onAiConnectionClicked = onAiConnectionClicked,
                    onEditAiConnectionClicked = onEditAiConnectionClicked,
                    onAddAiConnectionClicked = onAddAiConnectionClicked,
                    onUpdateTermOfCacheClicked = onUpdateTermClicked,
                    onDirectAiConnectionChecked = onDirectAiConnectionChecked,
                    onAiUsagePolicyChecked = onAiUsagePolicyClicked,
                )
            }
        }
    }

    NavigationHandler(
        navigationState = uiState.navigationState,
        onBack = onBack,
        onNavigateToAiConnectionDetails = onNavigateToAiConnectionDetails,
        onNavigateToBrowser = onNavigateToBrowser,
        onNavigationHandled = onNavigationHandled,
    )
}

private enum class ContentType {
    ENABLE_AI,
    AI_CONNECTION_ITEM,
    ADD_AI_CONNECTION,
    TERM_OF_CACHE,
    DIRECT_AI_CONNECTION,
    AI_PRIVACY_POLICY,
}

@Composable
internal fun AiSettingsContent(
    isAiEnabled: Boolean,
    items: List<AiConnectionUiModel>,
    termOfCacheValue: Int,
    isDirectConnectionEnabled: Boolean,
    isDirectConnectionVisible: Boolean,
    isAddConnectionVisible: Boolean,
    onAiEnabledChecked: (Boolean) -> Unit,
    onAiConnectionClicked: (AiConnectionUiModel) -> Unit,
    onEditAiConnectionClicked: (AiConnectionUiModel) -> Unit,
    onAddAiConnectionClicked: () -> Unit,
    onUpdateTermOfCacheClicked: () -> Unit,
    onDirectAiConnectionChecked: (Boolean) -> Unit,
    onAiUsagePolicyChecked: () -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp),
    ) {
        item(
            key = ContentType.ENABLE_AI,
            contentType = ContentType.ENABLE_AI,
        ) {
            SwitchItem(
                title = stringResource(id = R.string.ai_settings_title_enable_ai),
                isChecked = isAiEnabled,
                onChecked = onAiEnabledChecked,
            )
        }

        if (isAiEnabled) {
            items(
                items = items,
                key = AiConnectionUiModel::id,
                contentType = { ContentType.AI_CONNECTION_ITEM },
            ) {
                AiConnectionItem(
                    model = it,
                    onAiConnectionClicked = onAiConnectionClicked,
                    onEditAiConnectionClicked = onEditAiConnectionClicked,
                )

                if (!isAddConnectionVisible) {
                    HorizontalDivider()
                }
            }

            if (isAddConnectionVisible) {
                item(
                    key = ContentType.ADD_AI_CONNECTION,
                    contentType = ContentType.ADD_AI_CONNECTION,
                ) {
                    Column {
                        AddAiConnectionItem(onAddAiConnectionClicked = onAddAiConnectionClicked)
                        HorizontalDivider()
                    }
                }
            }
        }

        item(
            key = ContentType.TERM_OF_CACHE,
            contentType = ContentType.TERM_OF_CACHE,
        ) {
            TermOfCacheItem(
                termOfCacheValue = termOfCacheValue,
                onUpdateTermOfCacheClicked = onUpdateTermOfCacheClicked,
            )
        }

        if (isDirectConnectionVisible) {
            item(
                key = ContentType.DIRECT_AI_CONNECTION,
                contentType = ContentType.DIRECT_AI_CONNECTION,
            ) {
                SwitchItem(
                    title = stringResource(id = R.string.ai_settings_direct_connection),
                    isChecked = isDirectConnectionEnabled,
                    onChecked = onDirectAiConnectionChecked,
                )
            }
        }

        item(
            key = ContentType.AI_PRIVACY_POLICY,
            contentType = ContentType.AI_PRIVACY_POLICY,
        ) {
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onAiUsagePolicyChecked()
                    },
                headlineContent = {
                    Text(text = stringResource(id = R.string.ai_settings_privacy_policy))
                },
            )
        }
    }
}

@Composable
internal fun SwitchItem(
    title: String,
    isChecked: Boolean,
    onChecked: (Boolean) -> Unit,
) {
    ListItem(
        modifier = Modifier.fillMaxWidth(),
        headlineContent = {
            Text(text = title)
        },
        trailingContent = {
            Switch(
                checked = isChecked,
                onCheckedChange = onChecked,
            )
        },
    )
}

@Composable
internal fun AddAiConnectionItem(
    onAddAiConnectionClicked: () -> Unit,
) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onAddAiConnectionClicked()
            },
        headlineContent = {
            Text(text = stringResource(id = R.string.ai_settings_add_ai_connection))
        },
        leadingContent = {
            Icon(
                modifier = Modifier.size(size = 24.dp),
                imageVector = Icons.Default.Add,
                contentDescription = null,
            )
        },
    )
}

@Composable
internal fun TermOfCacheItem(
    termOfCacheValue: Int,
    onUpdateTermOfCacheClicked: () -> Unit,
) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onUpdateTermOfCacheClicked()
            },
        headlineContent = {
            Text(text = stringResource(id = R.string.ai_settings_term_of_cache))
        },
        supportingContent = {
            Text(
                text = pluralStringResource(
                    id = R.plurals.ai_settings_storage_term,
                    count = termOfCacheValue,
                    termOfCacheValue,
                )
            )
        },
    )
}

@Composable
internal fun AiConnectionItem(
    model: AiConnectionUiModel,
    onAiConnectionClicked: (AiConnectionUiModel) -> Unit,
    onEditAiConnectionClicked: (AiConnectionUiModel) -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
    ) {
        ListItem(
            modifier = Modifier
                .clickable {
                    onAiConnectionClicked(model)
                }
                .fillMaxWidth(),
            headlineContent = {
                Text(text = model.name)
            },
            supportingContent = if (model.status != null) {
                {
                    Text(text = model.status)
                }
            } else {
                null
            },
            trailingContent = {
                IconButton(
                    onClick = {
                        onEditAiConnectionClicked(model)
                    },
                ) {
                    Icon(
                        modifier = Modifier.size(size = 24.dp),
                        imageVector = Icons.Default.Settings,
                        tint = if (model.isActive) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            LocalContentColor.current
                        },
                        contentDescription = null,
                    )
                }
            },
        )
    }
}

@Composable
internal fun NavigationHandler(
    navigationState: NavigationState?,
    onBack: () -> Unit,
    onNavigateToAiConnectionDetails: (AiConnectionDetailsPayload) -> Unit,
    onNavigateToBrowser: (String) -> Unit,
    onNavigationHandled: () -> Unit,
) {
    LaunchedEffect(key1 = navigationState) {
        when (navigationState) {
            NavigationState.Back -> onBack()

            is NavigationState.NavigateToAiConnectionDetails -> onNavigateToAiConnectionDetails(
                AiConnectionDetailsPayload(
                    aiConnectionId = navigationState.id,
                ),
            )

            is NavigationState.NavigateToAiUsagePolicy -> {
                onNavigateToBrowser(navigationState.aiPrivacyUrl)
            }

            null -> Unit
        }

        navigationState?.let { onNavigationHandled() }
    }
}

@ThemePreviews
@Composable
private fun ContentPreview(
    @PreviewParameter(AiSettingsPreviewParameterProvider::class) items: List<AiConnectionUiModel>,
) {
    QuizApplicationTheme {
        QuizBackground {
            AiSettingsContent(
                items = items,
                isAiEnabled = true,
                termOfCacheValue = 1,
                isDirectConnectionEnabled = true,
                isAddConnectionVisible = true,
                isDirectConnectionVisible = false,
                onAiEnabledChecked = {},
                onAiConnectionClicked = {},
                onEditAiConnectionClicked = {},
                onAddAiConnectionClicked = {},
                onUpdateTermOfCacheClicked = {},
                onDirectAiConnectionChecked = {},
                onAiUsagePolicyChecked = {},
            )
        }
    }
}
