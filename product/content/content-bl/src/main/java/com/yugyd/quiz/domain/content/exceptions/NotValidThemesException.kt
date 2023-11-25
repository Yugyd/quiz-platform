package com.yugyd.quiz.domain.content.exceptions

import com.yugyd.quiz.domain.api.model.Theme

data class NotValidThemesException(
    override val message: String,
    val themes: Set<Theme>,
) : ContentVerificationException()
