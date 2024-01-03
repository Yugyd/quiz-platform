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

package com.yugyd.quiz.ui.theme

import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.yugyd.quiz.commonui.utils.ProgressUtils
import com.yugyd.quiz.ui.theme.model.ThemeUiModel
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme
import com.yugyd.quiz.uikit.theme.app_color_positive
import com.yugyd.quiz.uikit.theme.progressIndicatorHeight
import com.yugyd.quiz.uikit.R as UiKitR

private const val THEME_IMAGER_RATIO = 1.77F

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ThemeItem(
    model: ThemeUiModel,
    onStartClicked: (ThemeUiModel) -> Unit,
    onInfoClicked: (ThemeUiModel) -> Unit,
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onStartClicked(model) }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .aspectRatio(THEME_IMAGER_RATIO)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                    ),
            ) {
                if (model.imageUri != null) {
                    AsyncImage(
                        model = model.imageUri,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize(),
                        placeholder = painterResource(id = UiKitR.drawable.ic_no_photography_24),
                        error = painterResource(id = UiKitR.drawable.ic_no_photography_24),
                    )
                } else {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = model.title.first().toString().uppercase(),
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }

            Column(
                modifier = Modifier.padding(all = 16.dp)
            ) {
                Text(
                    text = model.title,
                    style = MaterialTheme.typography.headlineSmall,
                )

                Spacer(modifier = Modifier.height(height = 8.dp))

                Text(
                    text = model.subtitle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                )

                Spacer(modifier = Modifier.height(height = 16.dp))

                val animatedProgress by animateFloatAsState(
                    targetValue = ProgressUtils.toFloatPercent(model.progressPercent),
                    animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
                    label = "ProgressFloatAnimation",
                )
                LinearProgressIndicator(
                    progress = animatedProgress,
                    modifier = Modifier
                        .height(height = progressIndicatorHeight)
                        .fillMaxWidth(),
                    color = model.progressColor,
                )

                Spacer(modifier = Modifier.height(height = 16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = {
                            onInfoClicked(model)
                        },
                    ) {
                        Text(text = stringResource(id = R.string.action_info))
                    }

                    Spacer(modifier = Modifier.width(width = 8.dp))

                    Button(
                        onClick = {
                            onStartClicked(model)
                        },
                    ) {
                        Text(text = stringResource(id = com.yugyd.quiz.uikit.R.string.action_game))
                    }
                }
            }
        }
    }
}

@ThemePreviews
@Composable
private fun ThemeItemPreview() {
    QuizApplicationTheme {
        Surface {
            Box(modifier = Modifier.padding(all = 8.dp)) {
                val testTheme = ThemeUiModel(
                    id = 1,
                    title = "Title",
                    subtitle = "Subtitle",
                    imageUri = Uri.EMPTY,
                    progressPercent = 40,
                    record = 40,
                    progressColor = app_color_positive,
                )
                ThemeItem(
                    model = testTheme,
                    onStartClicked = {},
                    onInfoClicked = {},
                )
            }
        }
    }
}
