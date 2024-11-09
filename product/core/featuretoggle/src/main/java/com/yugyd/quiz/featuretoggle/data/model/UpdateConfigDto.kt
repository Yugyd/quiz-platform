package com.yugyd.quiz.featuretoggle.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateConfigDto(
    @SerialName("locale")
    val locale: String,
    @SerialName("mainScreen")
    val mainScreen: UpdateMainScreenDto,
    @SerialName("links")
    val links: List<LinkDto>,
)
