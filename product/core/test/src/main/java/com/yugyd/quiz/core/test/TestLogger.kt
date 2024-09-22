package com.yugyd.quiz.core.test

import com.yugyd.quiz.core.Logger

class TestLogger : Logger {
    override fun print(tag: String, message: String) = Unit
    override fun print(message: String) = Unit
    override fun logError(error: Throwable) = Unit
    override fun logError(tag: String, error: Throwable) = Unit
    override fun recordError(error: Throwable) = Unit
    override fun recordError(tag: String, error: Throwable) = Unit
}