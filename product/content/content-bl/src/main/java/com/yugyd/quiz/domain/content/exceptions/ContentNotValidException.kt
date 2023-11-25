package com.yugyd.quiz.domain.content.exceptions

data class ContentNotValidException(
    override val message: String,
    override val cause: Throwable?,
) : IllegalStateException()
