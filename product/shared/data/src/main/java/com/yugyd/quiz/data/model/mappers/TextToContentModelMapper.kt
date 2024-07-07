package com.yugyd.quiz.data.model.mappers

import com.yugyd.quiz.domain.api.model.Theme
import com.yugyd.quiz.domain.content.api.ContentDataModel
import com.yugyd.quiz.domain.content.api.RawContentDataModel
import com.yugyd.quiz.domain.game.api.model.Quest
import com.yugyd.quiz.domain.game.api.model.QuestType
import javax.inject.Inject

class TextToContentModelMapper @Inject constructor() {

    fun map(raw: RawContentDataModel): ContentDataModel {
        val quests = raw.rawQuests.mapIndexed { index, item ->
            item.mapToQuestEntity(id = index.inc())
        }
        return ContentDataModel(
            quests = quests,
            themes = raw.rawCategories.mapIndexed { index, item ->
                val mappedItem = item.mapToThemeEntity(ordinal = index)
                mappedItem.copy(
                    count = quests.count { it.category == mappedItem.id }
                )
            },
        )
    }

    private fun String.mapToQuestEntity(id: Int): Quest {
        val data = this.split(SPLITTER)

        return Quest(
            id = id,
            quest = data[0],
            trueAnswer = data[1],
            answer2 = data[2],
            answer3 = data[3],
            answer4 = data[4],
            answer5 = "",
            answer6 = "",
            answer7 = "",
            answer8 = "",
            complexity = data[5].toInt(),
            category = data[6].toInt(),
            section = data[7].toInt(),
            type = QuestType.SIMPLE,
        )
    }

    private fun String.mapToThemeEntity(ordinal: Int): Theme {
        val data = this.split(SPLITTER)

        return if (data.size == 3) {
            Theme(
                id = data[0].toInt(),
                ordinal = ordinal,
                name = data[1],
                info = data[2],
                image = null,
            )
        } else {
            Theme(
                id = data[0].toInt(),
                ordinal = ordinal,
                name = data[1],
                info = data[2],
                image = data[3],
            )
        }
    }

    companion object {
        private const val SPLITTER = "\n"
    }
}
