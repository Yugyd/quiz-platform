package com.yugyd.quiz.core

enum class AdProviderType {
    YANDEX,
    GOOGLE;

    companion object {

        fun from(value: String): AdProviderType {
            return when (value) {
                "yandex" -> YANDEX
                "google" -> GOOGLE
                else -> throw IllegalArgumentException()
            }
        }
    }
}
