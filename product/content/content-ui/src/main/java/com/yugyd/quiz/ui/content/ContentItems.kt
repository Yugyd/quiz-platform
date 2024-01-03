package com.yugyd.quiz.ui.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.yugyd.quiz.domain.content.api.ContentModel
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme
import com.yugyd.quiz.uikit.R as UiKitR

internal enum class ContentType {
    NEW_FILE_HEADER, CONTENT_ITEM
}

@Composable
internal fun NewFileItem(
    onOpenFileClicked: () -> Unit,
    onContentFormatClicked: () -> Unit,
) {
    OutlinedCard(
        modifier = Modifier.padding(all = 8.dp),
    ) {
        Column(
            modifier = Modifier.padding(all = 16.dp),
        ) {
            Text(
                text = stringResource(id = R.string.content_empty_state_new_file),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.content_empty_state_message),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )

            TextButton(
                onClick = onContentFormatClicked,
                shape = RectangleShape,
                contentPadding = PaddingValues(all = 0.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.content_empty_state_note),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            FilledTonalButton(
                modifier = Modifier.align(Alignment.End),
                onClick = onOpenFileClicked,
            ) {
                Text(text = stringResource(id = R.string.content_empty_state_button))
            }
        }
    }
}

@Composable
internal fun ContentItem(
    model: ContentModel,
    onItemClicked: (ContentModel) -> Unit,
) {
    ListItem(
        modifier = Modifier.clickable(
            onClick = {
                onItemClicked(model)
            }
        ),
        headlineContent = {
            Text(text = model.name)
        },
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(size = 40.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape,
                    )
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = model.name.first().toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        },
        trailingContent = if (model.isChecked) {
            {
                Box(
                    modifier = Modifier.size(size = 40.dp)
                ) {
                    Icon(
                        modifier = Modifier.align(Alignment.Center),
                        painter = painterResource(id = UiKitR.drawable.ic_check),
                        contentDescription = null,
                    )
                }
            }
        } else {
            null
        }
    )
}

@ThemePreviews
@Composable
private fun EmptyStateItemPreview() {
    QuizApplicationTheme {
        Surface {
            NewFileItem(
                onOpenFileClicked = {},
                onContentFormatClicked = {},
            )
        }
    }
}

@ThemePreviews
@Composable
private fun ContentItemPreview(
    @PreviewParameter(ContentPreviewParameterProvider::class) items: List<ContentModel>,
) {
    QuizApplicationTheme {
        Surface {
            ContentItem(
                model = items.first(),
                onItemClicked = {},
            )
        }
    }
}
