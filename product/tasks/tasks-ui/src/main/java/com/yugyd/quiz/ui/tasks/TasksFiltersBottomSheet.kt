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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.yugyd.quiz.ui.tasks.TasksView.State.FilterUiModel
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TasksFiltersBottomSheet(
    openBottomSheet: Boolean,
    filters: List<FilterUiModel>,
    onFilterClicked: (FilterUiModel) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = bottomSheetState,
        ) {
            TasksFiltersContent(
                filters = filters,
                onFilterClicked = onFilterClicked,
            )
        }
    }

    LaunchedEffect(openBottomSheet) {
        if (openBottomSheet) {
            bottomSheetState.expand()
        } else {
            bottomSheetState.hide()
        }
    }
}

private const val FILTER_CONTENT_TYPE = "FILTER_CONTENT_TYPE"
private const val FILTERS_COLUM_SIZE = 2

@Composable
internal fun TasksFiltersContent(
    filters: List<FilterUiModel>,
    onFilterClicked: (FilterUiModel) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            text = stringResource(id = R.string.tasks_filters),
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(count = FILTERS_COLUM_SIZE),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(
                items = filters,
                key = {
                    it.filterModel.id
                },
                contentType = { FILTER_CONTENT_TYPE },
            ) {
                FilterItem(
                    item = it,
                    onFilterClicked = onFilterClicked,
                )
            }
        }
    }
}

@Composable
private fun FilterItem(
    item: FilterUiModel,
    onFilterClicked: (FilterUiModel) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 0.dp,
                end = 16.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = item.filterModel.isChecked,
            onCheckedChange = {
                onFilterClicked(item)
            }
        )

        Spacer(modifier = Modifier.width(width = 16.dp))

        Text(
            modifier = Modifier.weight(1F),
            text = stringResource(id = item.titleRes),
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@ThemePreviews
@Composable
private fun TasksFiltersBottomSheetPreview(
    @PreviewParameter(FiltersPreviewParameterProvider::class) items: List<FilterUiModel>,
) {
    QuizApplicationTheme {
        Surface {
            TasksFiltersContent(
                filters = items,
                onFilterClicked = {},
            )
        }
    }
}
