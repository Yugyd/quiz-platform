package com.yugyd.quiz.data.model.mappers

import com.yugyd.quiz.domain.api.model.Quest
import com.yugyd.quiz.domain.api.model.Theme
import com.yugyd.quiz.domain.content.api.ContentDataModel
import com.yugyd.quiz.domain.content.api.RawContentDataModel
import javax.inject.Inject

class TextToContentModelMapper @Inject constructor() {

    fun map(raw: RawContentDataModel): ContentDataModel {
        return ContentDataModel(
            quests = raw.rawQuests.map { it.mapToQuestEntity() },
            themes = raw.rawCategories.map { it.mapToThemeEntity() },
        )
    }

    private fun String.mapToQuestEntity(): Quest {
        val data = this.split(SPLITTER)

        return Quest(
            id = data[0].toInt(),
            quest = data[1],
            trueAnswer = data[2],
            answer2 = data[3],
            answer3 = data[4],
            answer4 = data[5],
            answer5 = data[6],
            answer6 = data[7],
            answer7 = data[8],
            answer8 = data[9],
            complexity = data[10].toInt(),
            category = data[11].toInt(),
            section = data[12].toInt(),
        )
    }

    private fun String.mapToThemeEntity(): Theme {
        val data = this.split(SPLITTER)

        return Theme(
            id = data[0].toInt(),
            ordinal = data[1].toInt(),
            name = data[2],
            info = data[3],
            image = data[4],
            count = data[5].toInt(),
        )
    }

    companion object {
        private const val SPLITTER = ""
    }
}
