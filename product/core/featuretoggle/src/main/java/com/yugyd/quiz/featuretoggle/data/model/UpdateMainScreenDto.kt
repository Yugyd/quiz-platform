package com.yugyd.quiz.featuretoggle.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateMainScreenDto(
    @SerialName("buttonTitle")
    val buttonTitle: String,
    @SerialName("message")
    val message: String,
    @SerialName("title")
    val title: String,
)
