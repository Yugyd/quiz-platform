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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.QuizBackground
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AiTermOfCacheDialog(
    options: List<String>,
    selectedOption: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    onOptionSelected: (String) -> Unit,
) {
    BasicAlertDialog(
        onDismissRequest = {
            onDismissRequest()
        },
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 434.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(28.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(id = R.string.ai_settings_term_of_cache),
                    modifier = Modifier.padding(all = 24.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                HorizontalDivider()

                Column(
                    modifier = Modifier
                        .weight(weight = 1F)
                        .selectableGroup()
                        .verticalScroll(rememberScrollState()),
                ) {
                    options.forEach { option ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .selectable(
                                    selected = option == selectedOption,
                                    onClick = { onOptionSelected(option) },
                                    role = Role.RadioButton,
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = (option == selectedOption),
                                onClick = null,
                            )

                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }

                HorizontalDivider()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(id = android.R.string.cancel))
                    }

                    TextButton(
                        onClick = { onConfirmation() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(id = android.R.string.ok))
                    }
                }
            }
        }
    }
}

@Composable
internal fun AiInfoDialog(onInfoDialogDismissState: () -> Unit) {
    AlertDialog(
        onDismissRequest = {
            onInfoDialogDismissState()
        },
        title = {
            Text(
                text = stringResource(id = R.string.ai_settings_title_ai),
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.ai_settings_ai_test_info),
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onInfoDialogDismissState()
                }
            ) {
                Text(stringResource(id = android.R.string.ok))
            }
        },
    )
}

@ThemePreviews
@Composable
private fun ContentPreview() {
    QuizApplicationTheme {
        QuizBackground {
            AiTermOfCacheDialog(
                options = listOf("1", "2", "3"),
                selectedOption = "1",
                onDismissRequest = {},
                onConfirmation = {},
                onOptionSelected = {},
            )
        }
    }
}
