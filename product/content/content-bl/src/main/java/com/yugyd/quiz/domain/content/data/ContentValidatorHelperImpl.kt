package com.yugyd.quiz.domain.content.data

import com.yugyd.quiz.domain.api.model.Quest
import com.yugyd.quiz.domain.api.model.Theme
import com.yugyd.quiz.domain.content.api.ContentDataModel
import com.yugyd.quiz.domain.content.exceptions.DuplicateIdQuestsException
import com.yugyd.quiz.domain.content.exceptions.DuplicateIdThemesException
import com.yugyd.quiz.domain.content.exceptions.NotValidQuestsException
import com.yugyd.quiz.domain.content.exceptions.NotValidThemesException
import javax.inject.Inject

internal class ContentValidatorHelperImpl @Inject constructor() : ContentValidatorHelper {

    override fun validateContent(contentData: ContentDataModel) {
        // There should not be topics with the same ID
        verifyDuplicateIds(
            items = contentData.quests,
            keyGetter = {
                it.id
            },
            exceptionFactory = {
                throw DuplicateIdQuestsException(
                    message = "Find duplicated id quests: ${it.size}",
                    quests = it,
                )
            }
        )

        // There should not be topics with the same ID
        verifyDuplicateIds(
            items = contentData.themes,
            keyGetter = {
                it.id
            },
            exceptionFactory = {
                throw DuplicateIdThemesException(
                    message = "Find duplicated id themes: ${it.size}",
                    themes = it,
                )
            }
        )

        // There should be no valid quests
        verifyItems(
            items = contentData.quests,
            predicate = ::isQuestValid,
            exceptionFactory = {
                throw NotValidQuestsException(
                    message = "Find not valid quests",
                    quests = it,
                )
            }
        )

        // There should be no valid themes
        verifyItems(
            items = contentData.themes,
            predicate = ::isThemeValid,
            exceptionFactory = {
                NotValidThemesException(
                    message = "Find not valid themes",
                    themes = it,
                )
            }
        )
    }

    private fun <T : Any> verifyDuplicateIds(
        items: List<T>,
        keyGetter: (T) -> Int,
        exceptionFactory: (Set<T>) -> Throwable,
    ) {
        // Не должно быть айтемов с одним ид
        val map = hashMapOf<Int, List<T>>()
        items.forEach {
            val key = keyGetter(it)
            if (map.containsKey(key)) {
                val value = requireNotNull(map[key])
                map[key] = value.plus(it)
            } else {
                map[key] = listOf(it)
            }
        }

        val duplicateIdItems = map.values
            .filter {
                it.size > 1
            }
            .flatten()
            .toSet()

        if (duplicateIdItems.isNotEmpty()) {
            throw exceptionFactory(duplicateIdItems)
        }
    }

    private fun isQuestValid(quest: Quest) = quest.id.toString().isNotBlank()
            && quest.quest.isNotBlank()
            && quest.trueAnswer.isNotBlank()
            && quest.answer2.isNotBlank()
            && quest.answer3.isNotBlank()
            && quest.answer4.isNotBlank()

    private fun isThemeValid(theme: Theme) = theme.id.toString().isNotBlank()
            && theme.name.isNotBlank()
            && theme.info.isNotBlank()
            && theme.count.toString().isNotBlank()

    private fun <T : Any> verifyItems(
        items: List<T>,
        predicate: (T) -> Boolean,
        exceptionFactory: (Set<T>) -> Throwable,
    ) {
        val notValidThemes = items
            .filterNot {
                predicate(it)
            }
            .toSet()
        if (notValidThemes.isNotEmpty()) {
            throw exceptionFactory(notValidThemes)
        }
    }
}
