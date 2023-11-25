package com.yugyd.quiz.domain.content.data.helper

import com.yugyd.quiz.domain.content.api.ContentDataModel
import com.yugyd.quiz.domain.content.exceptions.DuplicateIdQuestsException
import com.yugyd.quiz.domain.content.exceptions.DuplicateIdThemesException
import com.yugyd.quiz.domain.content.exceptions.NotValidQuestsException
import com.yugyd.quiz.domain.content.exceptions.NotValidThemesException

interface ContentValidatorHelper {

    @Throws(
        DuplicateIdQuestsException::class,
        DuplicateIdThemesException::class,
        NotValidQuestsException::class,
        NotValidThemesException::class,
    )
    fun validateContent(contentData: ContentDataModel)
}
