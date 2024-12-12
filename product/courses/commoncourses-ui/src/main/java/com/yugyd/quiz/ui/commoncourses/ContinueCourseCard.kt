package com.yugyd.quiz.ui.commoncourses

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.yugyd.quiz.ui.commoncourses.models.ContinueCourseBannerUiModel
import com.yugyd.quiz.uikit.common.ThemePreviews
import com.yugyd.quiz.uikit.component.TwoLineWithActionsElevatedCard
import com.yugyd.quiz.uikit.theme.QuizApplicationTheme

@Composable
fun ContinueCourseCard(
    courseBanner: ContinueCourseBannerUiModel,
    onConfirmClicked: () -> Unit,
    onHideClicked: () -> Unit,
) {
    TwoLineWithActionsElevatedCard(
        modifier = Modifier.padding(all = 16.dp),
        title = courseBanner.title,
        subtitle = stringResource(id = R.string.commin_course_banner_title),
        confirm = stringResource(id = R.string.commin_course_banner_continue),
        cancel = stringResource(id = R.string.commin_course_banner_hide),
        onConfirmClicked = onConfirmClicked,
        onCancelClicked = onHideClicked,
    )
}

@ThemePreviews
@Composable
private fun OpenSourceProfileItemPreview(
    @PreviewParameter(ContinueCourseBannerPreviewParameterProvider::class)
    items: List<ContinueCourseBannerUiModel>,
) {
    QuizApplicationTheme {
        Surface {
            ContinueCourseCard(
                courseBanner = items.first(),
                onConfirmClicked = {},
                onHideClicked = {},
            )
        }
    }
}
