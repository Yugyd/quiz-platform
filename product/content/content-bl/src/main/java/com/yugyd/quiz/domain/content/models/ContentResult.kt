package com.yugyd.quiz.domain.content.models

import com.yugyd.quiz.domain.content.api.ContentModel

data class ContentResult(
    val isBack: Boolean,
    val newModel: ContentModel?,
)
