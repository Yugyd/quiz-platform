package com.yugyd.quiz.uikit.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme

@Composable
fun TwoLineWithActionsElevatedCard(
    title: String,
    subtitle: String,
    confirm: String,
    cancel: String,
    onConfirmClicked: () -> Unit,
    onCancelClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .align(Alignment.End),
                horizontalArrangement = Arrangement.End,
            ) {
                OutlinedButton(
                    onClick = onCancelClicked,
                ) {
                    Text(
                        text = cancel,
                    )
                }

                Spacer(modifier = Modifier.width(width = 8.dp))

                Button(
                    onClick = onConfirmClicked,
                ) {
                    Text(
                        text = confirm,
                    )
                }
            }
        }
    }
}

@ThemePreviews
@Composable
private fun TwoLineWithActionsElevatedCardPreview() {
    QuizApplicationTheme {
        Surface {
            TwoLineWithActionsElevatedCard(
                title = "Title",
                subtitle = "Subtitle",
                confirm = "Confirm",
                cancel = "Cancel",
                onConfirmClicked = {},
                onCancelClicked = {},
            )
        }
    }
}