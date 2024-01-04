package com.yugyd.quiz.domain.content.data

import com.yugyd.quiz.core.Logger
import com.yugyd.quiz.core.file.FileRepository
import com.yugyd.quiz.core.runCatch
import com.yugyd.quiz.data.model.mappers.TextToContentModelMapper
import com.yugyd.quiz.domain.api.repository.ContentResetSource
import com.yugyd.quiz.domain.api.repository.QuestSource
import com.yugyd.quiz.domain.api.repository.ThemeSource
import com.yugyd.quiz.domain.api.repository.UserResetSource
import com.yugyd.quiz.domain.content.ContentClient
import com.yugyd.quiz.domain.content.ContentSource
import com.yugyd.quiz.domain.content.api.ContentModel
import com.yugyd.quiz.domain.content.api.RawContentDataModel
import com.yugyd.quiz.domain.content.exceptions.ContentNotValidException
import com.yugyd.quiz.domain.content.exceptions.DuplicateIdQuestsException
import com.yugyd.quiz.domain.content.exceptions.DuplicateIdThemesException
import com.yugyd.quiz.domain.content.exceptions.NotValidQuestsException
import com.yugyd.quiz.domain.content.exceptions.NotValidThemesException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ContentClientImpl @Inject constructor(
    private val fileRepository: FileRepository,
    private val textToContentEntityMapper: TextToContentModelMapper,
    private val contentResetDataSource: ContentResetSource,
    private val themeDataSource: ThemeSource,
    private val questDataSource: QuestSource,
    private val userResetSource: UserResetSource,
    private val contentSource: ContentSource,
    private val contentValidatorHelper: ContentValidatorHelper,
    private val logger: Logger,
) : ContentClient {

    override suspend fun isSelected(): Boolean {
        val isSelected = contentSource.getSelectedContent() != null
        logger.print(TAG, "Is selected content: $isSelected")
        return isSelected
    }

    override suspend fun getSelectedContent(): ContentModel? {
        val data = contentSource.getSelectedContent()
        logger.print(TAG, "Get selected content: $data")
        return data
    }

    override fun subscribeToSelectedContent(): Flow<ContentModel?> {
        return contentSource
            .subscribeToSelectedContent()
            .onEach {
                logger.print(TAG, "Subscribe to selected content. New selected item: $it")
            }
            .distinctUntilChanged()
    }

    override suspend fun getContents(): List<ContentModel> {
        val data = contentSource.getContents()
        logger.print(TAG, "Get contents: $data")
        return data
    }

    override fun subscribeToContents(): Flow<List<ContentModel>> {
        return contentSource
            .subscribeToContents()
            .onEach {
                logger.print(TAG, "Subscribe to contents. New items: $it")
            }
            .distinctUntilChanged()
    }

    override suspend fun setContent(newModel: ContentModel, oldModel: ContentModel): Boolean {
        logger.print(TAG, "Set content started. New: $newModel, old: $oldModel")

        // Read content file from external storage
        val filePath = newModel.filePath
        val rawText = fileRepository.readTextFromFile(newModel.filePath)

        // Generate content tag
        val contentMarker = rawText.hashCode().toString()

        logger.print(
            tag = TAG,
            message = "Load data from $filePath. Content marker: $contentMarker",
        )

        // Check that this is not current content
        if (!isNewSelected(contentMarker)) return false

        runCatch(
            block = {
                // Process content
                processContent(
                    rawText = rawText,
                    contentMarker = contentMarker,
                )

                val checkedNewModel = newModel.copy(isChecked = true)
                val uncheckedOldModel = oldModel.copy(isChecked = false)
                contentSource.updateContent(checkedNewModel)
                contentSource.updateContent(uncheckedOldModel)

                logger.print(TAG, "Set content successful. New content: $checkedNewModel")
            },
            catch = {
                logger.recordError(TAG, it)
                logger.print(TAG, "Set content failed")

                when (it) {
                    is IOException -> {
                        logger.print(TAG, "Delete new content item")

                        contentSource.deleteContent(newModel.id)
                        throw ContentNotValidException(
                            message = "Delete old content item is ${newModel}",
                            cause = it,
                        )
                    }

                    else -> {
                        throw it
                    }
                }
            }
        )

        return true
    }

    private suspend fun isNewSelected(contentMarker: String): Boolean {
        val oldContentMarker = contentSource.getSelectedContent()?.contentMarker
        return if (oldContentMarker == contentMarker) {
            logger.print(
                tag = TAG,
                message = "Content is already selected. Old: $oldContentMarker, new: $contentMarker",
            )
            false
        } else {
            true
        }
    }

    private suspend fun isExists(contentMarker: String): Boolean {
        val contentMarkers = contentSource.getContents().map(ContentModel::contentMarker)
        return if (contentMarkers.contains(contentMarker)) {
            logger.print(
                tag = TAG,
                message = "Content is already selected. Olds: $contentMarkers, new: $contentMarker",
            )
            false
        } else {
            true
        }
    }

    override suspend fun setContent(
        oldModel: ContentModel?,
        contentName: String?,
        uri: String,
    ): Boolean {
        logger.print(TAG, "Set content started. Old: $oldModel, name: $contentName, uri: $uri")

        // Read content file from external storage
        val rawText = fileRepository.readTextFromUri(uri)

        // Generate content tag
        val contentMarker = rawText.hashCode().toString()

        logger.print(
            tag = TAG,
            message = "Load data from uri: $uri. Content marker: $contentMarker",
        )

        // Check that this is not current content
        if (!isNewSelected(contentMarker) || !isExists(contentMarker)) return false

        // Copy file
        val localStorageFile = getFileName(
            contentMarker = contentMarker,
            uri = uri,
        )
        logger.print(TAG, "Local content file name: $localStorageFile")

        val internalFile = fileRepository.saveTextToLocalStorage(
            fileName = localStorageFile,
            fileContents = rawText,
        )
        logger.print(TAG, "Internal file path: ${internalFile.path}")

        // Process content
        processContent(
            rawText = rawText,
            contentMarker = contentMarker,
        )

        if (oldModel != null) {
            val uncheckedOldModel = oldModel.copy(isChecked = false)
            contentSource.updateContent(uncheckedOldModel)
        }

        val name = contentName ?: localStorageFile.substringBeforeLast(".")
        val checkedNewModel = ContentModel(
            name = name,
            filePath = internalFile.path,
            isChecked = true,
            contentMarker = contentMarker,
        )
        contentSource.addContent(checkedNewModel)

        logger.print(TAG, "Set content successful. New content: $checkedNewModel")

        return true
    }

    override suspend fun deleteContent(id: String) {
        logger.print(TAG, "Delete content: $id")

        contentSource.deleteContent(id)
    }

    @Throws(
        DuplicateIdQuestsException::class,
        DuplicateIdThemesException::class,
        NotValidQuestsException::class,
        NotValidThemesException::class,
    )
    private suspend fun processContent(rawText: String, contentMarker: String) {
        logger.print(
            tag = TAG,
            message = "Process content is started. Raw text length: ${rawText.length}, marker: $contentMarker",
        )

        // Map the data into the model
        val rawData = getRawData(rawText)
        val contentData = textToContentEntityMapper.map(rawData)
        logger.print(
            tag = TAG,
            message = "Raw data mapped. Quest: ${contentData.quests.size}, category: ${contentData.themes.size}",
        )

        // Data validation, filtering of invalid data
        contentValidatorHelper.validateContent(contentData)

        // Reset data to database content
        contentResetDataSource.reset()

        // Write models to database
        themeDataSource.addThemes(contentData.themes)
        questDataSource.addQuests(contentData.quests)

        // Reset progress
        userResetSource.reset()

        logger.print(TAG, "Reset content and user progress. Add content to database")
    }

    private fun getFileName(contentMarker: String, uri: String): String {
        val uriFileName = requireNotNull(fileRepository.getFileName(uri))
        return contentMarker + FILE_SEPARATOR + uriFileName
    }

    private fun getRawData(rawText: String): RawContentDataModel {
        val rawCategoryBlock = getCategoryBlock(rawText).trim()
        val rawCategories = rawCategoryBlock.split(ITEM_SPLITTER)

        val rawQuestBlock = getQuestBlock(rawText).trim()
        val rawQuests = rawQuestBlock.split(ITEM_SPLITTER)

        return RawContentDataModel(
            rawCategories = rawCategories,
            rawQuests = rawQuests,
        )
    }

    private fun getCategoryBlock(rawText: String) = rawText
        .substringAfter(CATEGORY_SECTION)
        .substringBefore(QUEST_SECTION)

    private fun getQuestBlock(rawText: String) = rawText.substringAfter(QUEST_SECTION)

    companion object {
        private const val TAG = "ContentClientImpl"
        private const val FILE_SEPARATOR = "-"
        private const val CATEGORY_SECTION = "[category]"
        private const val QUEST_SECTION = "[quest]"
        private const val ITEM_SPLITTER = "\n\n"
    }
}
