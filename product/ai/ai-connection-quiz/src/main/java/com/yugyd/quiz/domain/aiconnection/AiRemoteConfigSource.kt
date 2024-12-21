package com.yugyd.quiz.domain.aiconnection

import com.yugyd.quiz.domain.aiconnection.model.AiInstructionConfig

internal interface AiRemoteConfigSource {
    suspend fun getAiInstructionConfigs(): List<AiInstructionConfig>
    suspend fun getAiPrivacyUrl(): String?
}
