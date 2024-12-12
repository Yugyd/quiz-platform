package com.yugyd.quiz.ui.commoncourses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.yugyd.quiz.ui.commoncourses.models.CourseUiModel
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme

@Composable
fun CourseItem(
    model: CourseUiModel,
    onItemClicked: (CourseUiModel) -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
    ) {
        ListItem(
            modifier = Modifier
                .clickable {
                    onItemClicked(model)
                }
                .fillMaxWidth(),
            headlineContent = {
                Text(text = model.name)
            },
        )
    }
}

@ThemePreviews
@Composable
private fun OpenSourceProfileItemPreview(
    @PreviewParameter(CourseUiModelPreviewParameterProvider::class)
    items: List<CourseUiModel>,
) {
    QuizApplicationTheme {
        Surface {
            CourseItem(
                model = items.first(),
                onItemClicked = {},
            )
        }
    }
}
