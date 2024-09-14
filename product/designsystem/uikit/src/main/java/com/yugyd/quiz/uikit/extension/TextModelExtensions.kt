package com.yugyd.quiz.uikit.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import com.yugyd.quiz.core.TextModel

@ReadOnlyComposable
@Composable
fun TextModel.getText(): String {
    return when (this) {
        is TextModel.ResTextModel -> stringResource(id = res)
        is TextModel.StringTextModel -> value
    }
}
