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

package com.yugyd.quiz.newversiononboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yugyd.quiz.domain.api.payload.OnboardingPayload
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme
import com.yugyd.quiz.uikit.R as UiKitR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingBottomSheet(
    openBottomSheet: Boolean,
    payload: OnboardingPayload?,
    onButtonClicked: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (openBottomSheet && payload != null) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = bottomSheetState,
        ) {
            OnboardingContent(
                payload = payload,
                onButtonClicked = onButtonClicked,
                onDismissRequest = onDismissRequest,
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

@Composable
internal fun OnboardingContent(
    payload: OnboardingPayload,
    onButtonClicked: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 24.dp
            )
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(id = UiKitR.drawable.ic_auto_awesome_24),
            contentDescription = null,
            modifier = Modifier.size(96.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = payload.title,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineSmall,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = payload.message,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
        )

        if (payload.buttonTitle != null) {
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onButtonClicked()
                    onDismissRequest()
                },
            ) {
                Text(text = payload.buttonTitle!!)
            }
        }
    }
}

@ThemePreviews
@Composable
private fun OnboardingContentPreview() {
    QuizApplicationTheme {
        Surface {
            OnboardingContent(
                payload = OnboardingPayload(
                    title = "Title",
                    message = "Message",
                    buttonTitle = "Button title",
                ),
                onButtonClicked = {},
                onDismissRequest = {},
            )
        }
    }
}
