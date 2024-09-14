package com.yugyd.quiz.core

import androidx.annotation.StringRes

sealed interface TextModel {
    data class ResTextModel(@StringRes val res: Int) : TextModel
    data class StringTextModel(val value: String) : TextModel
}
