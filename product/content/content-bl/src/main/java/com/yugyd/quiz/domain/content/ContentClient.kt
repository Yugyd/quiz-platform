package com.yugyd.quiz.domain.content

import com.yugyd.quiz.domain.content.api.ContentModel
import com.yugyd.quiz.domain.content.exceptions.ContentNotValidException
import com.yugyd.quiz.domain.content.exceptions.DuplicateIdQuestsException
import com.yugyd.quiz.domain.content.exceptions.DuplicateIdThemesException
import com.yugyd.quiz.domain.content.exceptions.NotValidQuestsException
import com.yugyd.quiz.domain.content.exceptions.NotValidThemesException
import kotlinx.coroutines.flow.Flow

interface ContentClient {

    suspend fun isSelected(): Boolean

    suspend fun getSelectedContent(): ContentModel?

    fun subscribeToSelectedContent(): Flow<ContentModel?>

    suspend fun getContents(): List<ContentModel>

    fun subscribeToContents(): Flow<List<ContentModel>>

    /**
     * @param newModel previously uploaded content.
     * @return true if new content is installed, false if the content is already current.
     */
    @Throws(
        ContentNotValidException::class,
        DuplicateIdQuestsException::class,
        DuplicateIdThemesException::class,
        NotValidQuestsException::class,
        NotValidThemesException::class,
    )
    suspend fun setContent(newModel: ContentModel, oldModel: ContentModel): Boolean

    /**
     * @param uri new content received from external storage.
     * @return true if new content is installed, false if the content is already current.
     */
    @Throws(
        DuplicateIdQuestsException::class,
        DuplicateIdThemesException::class,
        NotValidQuestsException::class,
        NotValidThemesException::class,
    )
    suspend fun setContent(
        oldModel: ContentModel?,
        contentName: String?,
        uri: String,
    ): Boolean

    suspend fun deleteContent(id: String)
}
