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

package com.yugyd.quiz.profileui.profile

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.yugyd.quiz.profileui.model.HeaderProfileUiModel
import com.yugyd.quiz.profileui.model.ProfileUiModel
import com.yugyd.quiz.profileui.model.SectionProfileUiModel
import com.yugyd.quiz.profileui.model.SelectItemProfileUiModel
import com.yugyd.quiz.profileui.model.SocialItemProfileUiModel
import com.yugyd.quiz.profileui.model.SwitchItemProfileUiModel
import com.yugyd.quiz.profileui.model.ValueItemProfileUiModel

@Composable
internal fun ProfileItem(
    model: ProfileUiModel,
    onItemClicked: (ProfileUiModel) -> Unit,
    onItemChecked: (SwitchItemProfileUiModel, Boolean) -> Unit,
) {
    when (model) {
        is HeaderProfileUiModel -> {
            HeaderProfileItem(model)
        }

        is SectionProfileUiModel -> {
            SectionProfileItem(model.title)
        }

        is SelectItemProfileUiModel -> {
            SelectProfileItem(
                model = model,
                onItemClicked = onItemClicked
            )
        }

        is ValueItemProfileUiModel -> {
            ValueProfileItem(
                model = model,
                onItemClicked = onItemClicked
            )
        }

        is SwitchItemProfileUiModel -> {
            SwitchProfileItem(
                model = model,
                onItemChecked = onItemChecked,
            )
        }

        is SocialItemProfileUiModel -> {
            TelegramProfileItem(
                model = model,
                onItemClicked = onItemClicked,
            )
        }
    }
}

@Composable
internal fun HeaderProfileItem(model: HeaderProfileUiModel) {
    Surface(
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val drawable = AppCompatResources.getDrawable(
                LocalContext.current,
                model.appIcon
            )

            Image(
                painter = rememberDrawablePainter(drawable = drawable),
                contentDescription = null,
                modifier = Modifier.size(size = 96.dp),
            )

            Spacer(modifier = Modifier.height(height = 16.dp))

            Text(
                text = model.appName,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
            )

            Spacer(modifier = Modifier.height(height = 16.dp))

            Text(
                text = model.versionTitle,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
internal fun SectionProfileItem(sectionTitle: String) {
    Column {
        Divider()
        ListItem(
            headlineContent = {
                Text(
                    text = sectionTitle,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        )
    }
}

@Composable
internal fun SelectProfileItem(
    model: SelectItemProfileUiModel,
    onItemClicked: (ProfileUiModel) -> Unit,
) {
    ListItem(
        headlineContent = {
            Text(text = model.title)
        },
        modifier = Modifier.clickable {
            onItemClicked(model)
        }
    )
}

@Composable
internal fun ValueProfileItem(
    model: ValueItemProfileUiModel,
    onItemClicked: (ProfileUiModel) -> Unit,
) {
    ListItem(
        headlineContent = {
            Text(text = model.title)
        },
        modifier = Modifier.clickable {
            onItemClicked(model)
        },
        trailingContent = {
            Text(
                text = model.value,
                color = MaterialTheme.colorScheme.primary
            )
        }
    )
}

@Composable
internal fun SwitchProfileItem(
    model: SwitchItemProfileUiModel,
    onItemChecked: (SwitchItemProfileUiModel, Boolean) -> Unit,
) {
    ListItem(
        headlineContent = {
            Text(text = model.title)
        },
        trailingContent = {
            Switch(
                checked = model.isChecked,
                onCheckedChange = {
                    onItemChecked(model, it)
                }
            )
        }
    )
}

@Composable
internal fun TelegramProfileItem(
    model: SocialItemProfileUiModel,
    onItemClicked: (ProfileUiModel) -> Unit,
) {
    ListItem(
        headlineContent = {
            Text(text = model.title)
        },
        modifier = Modifier.clickable {
            onItemClicked(model)
        },
        supportingContent = {
            Text(text = model.message)
        },
        leadingContent = {
            Image(
                painter = painterResource(id = model.icon),
                contentDescription = null,
                modifier = Modifier.size(40.dp),
            )
        },
    )
}
