package com.yugyd.quiz.domain.aiconnection.data

import com.yugyd.quiz.core.Logger
import com.yugyd.quiz.core.result
import com.yugyd.quiz.domain.aiconnection.AiRemoteConfigSource
import com.yugyd.quiz.domain.aiconnection.data.entity.AiInstructionConfigDto
import com.yugyd.quiz.domain.aiconnection.data.mapper.AiInstructionConfigMapper
import com.yugyd.quiz.domain.aiconnection.model.AiInstructionConfig
import com.yugyd.quiz.remoteconfig.api.RemoteConfig
import kotlinx.serialization.json.Json
import javax.inject.Inject

internal class AiRemoteConfigDataSource @Inject constructor(
    private val remoteConfig: RemoteConfig,
    private val aiInstructionConfigMapper: AiInstructionConfigMapper,
    private val logger: Logger,
    private val json: Json,
) : AiRemoteConfigSource {

    override suspend fun getAiInstructionConfigs(): List<AiInstructionConfig> {
        return result {
            val dtoList = remoteConfig.fetchStringValue(API_KEY_INSTRUCTION_URL_KEY).run {
                json.decodeFromString<List<AiInstructionConfigDto>>(this)
            }

            val mappedDtoList = dtoList.map(aiInstructionConfigMapper::map)

            mappedDtoList
        }
            .getOrElse {
                logger.logError(it)

                emptyList()
            }
    }

    override suspend fun getAiPrivacyUrl(): String? {
        return result {
            remoteConfig.fetchStringValue(AI_PRIVACY_URL_KEY)
        }
            .getOrElse {
                logger.logError(it)

                null
            }
    }

    companion object {
        private const val API_KEY_INSTRUCTION_URL_KEY = "api_key_instruction_url"
        private const val AI_PRIVACY_URL_KEY = "ai_privacy_url"
    }
}

