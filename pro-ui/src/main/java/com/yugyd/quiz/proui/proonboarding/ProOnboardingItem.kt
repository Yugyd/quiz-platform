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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yugyd.quiz.proui.R
import com.yugyd.quiz.proui.proonboarding.model.ProOnboardingUiModel
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.IconWithBackground
import com.yugyd.quiz.uikit.component.QuizBackground
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme

@Composable
internal fun ProOnboardingItem(model: ProOnboardingUiModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (model.showBackground) {
            IconWithBackground(
                modifier = Modifier.size(size = 96.dp),
                icon = model.icon,
                contentDescription = null,
            )
        } else {
            Image(
                painter = painterResource(id = model.icon),
                contentDescription = null,
                modifier = Modifier.size(size = 96.dp),
            )
        }

        Spacer(modifier = Modifier.height(height = 32.dp))

        Text(
            text = model.title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineSmall,
        )

        Spacer(modifier = Modifier.height(height = 16.dp))

        Text(
            text = model.subtitle,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@ThemePreviews
@Composable
private fun ContentPreview() {
    QuizApplicationTheme {
        QuizBackground {
            ProOnboardingItem(
                model = ProOnboardingUiModel(
                    id = 0,
                    icon = R.drawable.ic_rocket_launch_48,
                    title = "Title",
                    subtitle = "Subtitle",
                    showBackground = true,
                )
            )
        }
    }
}
