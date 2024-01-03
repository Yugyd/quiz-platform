package com.yugyd.quiz.ui.content

import androidx.lifecycle.SavedStateHandle
import com.yugyd.quiz.commonui.test.currentState
import com.yugyd.quiz.core.test.TestDispatchersProvider
import com.yugyd.quiz.core.test.TestLogger
import com.yugyd.quiz.domain.content.ContentInteractor
import com.yugyd.quiz.domain.content.api.ContentModel
import com.yugyd.quiz.domain.content.exceptions.ContentNotValidException
import com.yugyd.quiz.domain.content.exceptions.DuplicateIdThemesException
import com.yugyd.quiz.domain.content.models.ContentResult
import com.yugyd.quiz.ui.content.ContentView.Action
import com.yugyd.quiz.ui.content.ContentView.State.NavigationState
import com.yugyd.quiz.ui.content.ContentView.State.SnackbarState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.test.runTest
import java.io.IOException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ContentViewModelTest {

    // Tests
    private val testIsBackEnabled = true
    private var testUri = "file://foo/bar.txt"
    private var testContents: List<ContentModel> = buildList {
        repeat(10) {
            add(
                buildContent(
                    index = it,
                    isChecked = it == 0,
                )
            )
        }
    }
    private var testError: Throwable? = null
    private var testSelectContent = true
    private var testAddContent = true
    private val testContentFormatUrl = "https://foo.bar/"

    // Fakes
    private lateinit var savedStateHandle: SavedStateHandle

    private var interactor: ContentInteractor = object : ContentInteractor {

        private val contentsFlow by lazy {
            MutableStateFlow(testContents)
        }

        override suspend fun getContentNameFromUri(uri: String): String? {
            return testUri.substringAfterLast("/")
        }

        override suspend fun isSelected(): Boolean {
            TODO("Not yet implemented")
        }

        override suspend fun getSelectedContent(): ContentModel? {
            TODO("Not yet implemented")
        }

        override suspend fun getContents(): List<ContentModel> {
            return if (testError != null) {
                throw testError!!
            } else {
                testContents
            }
        }

        override fun subscribeToContents(): Flow<List<ContentModel>> {
            return contentsFlow
                .onStart {
                    if (testError != null) {
                        throw testError!!
                    }
                }
        }

        override fun subscribeToSelectedContent(): Flow<ContentModel?> {
            TODO("Not yet implemented")
        }

        override fun isResetNavigation(
            oldModel: ContentModel?,
            newModel: ContentModel?
        ): ContentResult {
            TODO("Not yet implemented")
        }

        override suspend fun deleteContent(id: String) {
            if (testError != null) {
                throw testError!!
            } else {
                val deletedItem = testContents.first { it.id == id }
                testContents = testContents.minus(deletedItem)
                contentsFlow.value = testContents
            }
        }

        override suspend fun addContent(
            oldModel: ContentModel?,
            contentName: String?,
            uri: String,
        ): Boolean {
            return if (testError != null) {
                throw testError!!
            } else {
                if (testAddContent) {
                    testContents = testContents.plus(
                        buildContent(
                            index = 11,
                            filePath = uri,
                            isChecked = true,
                        )
                    )
                    contentsFlow.value = testContents
                }
                testAddContent
            }
        }

        override suspend fun selectContent(
            oldModel: ContentModel,
            newModel: ContentModel
        ): Boolean {
            return if (testError != null) {
                throw testError!!
            } else {
                if (testSelectContent) {
                    testContents = testContents.plus(newModel)
                    contentsFlow.value = testContents
                }
                testSelectContent
            }
        }

        override suspend fun getContentFormatUrl(): String {
            return if (testError != null) {
                throw testError!!
            } else {
                testContentFormatUrl
            }
        }
    }

    private lateinit var viewModel: ContentViewModel

    @BeforeTest
    fun setUp() {
        savedStateHandle = SavedStateHandle().apply {
            set(IS_BACK_ENABLED_ARG, testIsBackEnabled)
        }
    }

    private fun createViewModel() {
        viewModel = ContentViewModel(
            savedStateHandle = savedStateHandle,
            interactor = interactor,
            logger = TestLogger(),
            dispatchersProvider = TestDispatchersProvider(),
        )
    }

    @Test
    fun initialState_setsIsBackEnabledFromArgsInState() {
        // When
        createViewModel()

        // Then
        assertEquals(testIsBackEnabled, viewModel.currentState.isBackEnabled)
    }

    @Test
    fun loadData_setsContentsInState() {
        // When
        createViewModel()

        // Then
        val state = viewModel.currentState
        assertContentEquals(testContents, state.items)
        assertFalse(state.isWarning)
        assertFalse(state.isLoading)
    }

    @Test
    fun loadData_setsIsWarningInState() {
        // Given
        testError = IOException()

        // When
        createViewModel()

        // Then
        val state = viewModel.currentState
        assertNull(state.items)
        assertTrue(state.isWarning)
        assertFalse(state.isLoading)
    }

    @Test
    fun onBackClicked_setsNavigationBackInState() {
        // Given
        createViewModel()

        // When
        viewModel.onAction(Action.OnBackClicked)

        // Then
        assertEquals(NavigationState.Back, viewModel.currentState.navigationState)
    }

    @Test
    fun onItemClicked_stateNotChangedAfterReselectItem() = runTest {
        // Given
        createViewModel()

        // When
        val currentState = viewModel.currentState
        val selectedItem = currentState.items!!.first(ContentModel::isChecked)
        viewModel.onAction(Action.OnItemClicked(selectedItem))

        // Then
        assertEquals(currentState, viewModel.currentState)
    }

    @Test
    fun onItemClicked_setsNavigationBackInState() {
        // Given
        testSelectContent = true
        createViewModel()

        // When
        val clickedItem = testContents.filterNot(ContentModel::isChecked).first()
        viewModel.onAction(Action.OnItemClicked(clickedItem))

        // Then
        assertEquals(NavigationState.Back, viewModel.currentState.navigationState)
    }

    @Test
    fun onItemClicked_setsNotAddedContentsMessageInState() {
        // Given
        testSelectContent = false
        createViewModel()

        // When
        val clickedItem = testContents.filterNot(ContentModel::isChecked).first()
        viewModel.onAction(Action.OnItemClicked(clickedItem))

        // Then
        assertEquals(SnackbarState.NotAddedContentIsExists, viewModel.currentState.snackbarState)
    }

    @Test
    fun onItemClicked_setsVerifyErrorMessageInState() {
        // Given
        createViewModel()
        val duplicateIdThemesException = DuplicateIdThemesException(
            message = "Foo",
            themes = emptySet(),
        )
        testError = duplicateIdThemesException

        // When
        val clickedItem = testContents.filterNot(ContentModel::isChecked).first()
        viewModel.onAction(Action.OnItemClicked(clickedItem))

        // Then
        assertEquals(
            SnackbarState.VerifyError(duplicateIdThemesException),
            viewModel.currentState.snackbarState
        )
    }

    @Test
    fun onItemClicked_setsNotSelectAndDeleteMessageInState() {
        // Given
        createViewModel()
        testError = ContentNotValidException(
            message = "Foo",
            cause = null,
        )

        // When
        val clickedItem = testContents.filterNot(ContentModel::isChecked).first()
        viewModel.onAction(Action.OnItemClicked(clickedItem))

        // Then
        assertEquals(SnackbarState.NotSelectAndDelete, viewModel.currentState.snackbarState)
    }

    @Test
    fun onItemClicked_setsSelectIsFailedMessageInState() {
        // Given
        createViewModel()
        testError = IOException()

        // When
        val clickedItem = testContents.filterNot(ContentModel::isChecked).first()
        viewModel.onAction(Action.OnItemClicked(clickedItem))

        // Then
        assertEquals(SnackbarState.SelectIsFailed, viewModel.currentState.snackbarState)
    }

    @Test
    fun onDeleteClicked_setsContentsWithoutDeletedItemInState() {
        // Given
        testError = null
        createViewModel()

        // When
        val clickedItem = testContents.filterNot(ContentModel::isChecked).first()
        viewModel.onAction(Action.OnDeleteClicked(clickedItem))

        // Then
        assertTrue {
            viewModel.currentState.items!!.none { it == clickedItem }
        }
    }

    @Test
    fun onDeleteClicked_setsDeleteIsFailedMessageInState() {
        // Given
        testError = IOException()
        createViewModel()

        // When
        val clickedItem = testContents.filterNot(ContentModel::isChecked).first()
        viewModel.onAction(Action.OnDeleteClicked(clickedItem))

        // Then
        assertEquals(SnackbarState.DeleteIsFailed, viewModel.currentState.snackbarState)
    }

    @Test
    fun onDeleteClicked_setsSelectedItemNotDeleteMessageInState() {
        // Given
        testError = null
        createViewModel()

        // When
        val selectedItem = testContents.first(ContentModel::isChecked)
        viewModel.onAction(Action.OnDeleteClicked(selectedItem))

        // Then
        assertEquals(SnackbarState.SelectedItemNotDelete, viewModel.currentState.snackbarState)
    }

    @Test
    fun onDeleteClicked_setsOneItemNotDeleteMessageInState() {
        // Given
        testError = null
        testContents = testContents.subList(0, 1)
        createViewModel()
        assertEquals(1, viewModel.currentState.items!!.size)

        // When
        // Use selected item, test not show SelectedItemNotDelete message
        val selectedItem = testContents.first(ContentModel::isChecked)
        viewModel.onAction(Action.OnDeleteClicked(selectedItem))

        // Then
        assertEquals(SnackbarState.OneItemNotDelete, viewModel.currentState.snackbarState)
    }

    @Test
    fun onItemClicked_setsSnackbarStateIsNullInState() {
        // Given
        createViewModel()
        testError = IOException()
        val clickedItem = testContents.filterNot(ContentModel::isChecked).first()
        viewModel.onAction(Action.OnItemClicked(clickedItem))
        assertNotNull(viewModel.currentState.snackbarState)

        // When
        viewModel.onAction(Action.OnSnackbarDismissed)

        // Then
        assertNull(viewModel.currentState.snackbarState)
    }

    @Test
    fun onNavigationHandled_setsNavigationStateIsNullInState() {
        // Given
        createViewModel()
        viewModel.onAction(Action.OnBackClicked)
        assertNotNull(viewModel.currentState.navigationState)

        // When
        viewModel.onAction(Action.OnNavigationHandled)

        // Then
        assertNull(viewModel.currentState.navigationState)
    }

    @Test
    fun onOpenFileClicked_setsStateFileProviderIsTrueInState() {
        // Given
        createViewModel()

        // When
        viewModel.onAction(Action.OnOpenFileClicked)

        // Then
        assertTrue(viewModel.currentState.startFileProvider)
    }

    @Test
    fun onStartFileProviderHandled_setsStateFileProviderIsFalseInState() {
        // Given
        createViewModel()
        viewModel.onAction(Action.OnOpenFileClicked)
        assertTrue(viewModel.currentState.startFileProvider)

        // When
        viewModel.onAction(Action.OnStartFileProviderHandled)

        // Then
        assertFalse(viewModel.currentState.startFileProvider)
    }

    @Test
    fun onDocumentResult_setsUriIsNullMessageInState() {
        // Given
        createViewModel()

        // When
        viewModel.onAction(Action.OnDocumentResult(null))

        // Then
        assertEquals(SnackbarState.UriIsNull, viewModel.currentState.snackbarState)
    }

    @Test
    fun onDocumentResult_setsNewContentWhenEmptyContentsInState() {
        // Given
        testContents = emptyList()
        createViewModel()
        assertEquals(emptyList(), viewModel.currentState.items)

        // When
        viewModel.onAction(Action.OnDocumentResult(testUri))

        // Then
        assertTrue {
            viewModel.currentState.items!!.any {
                it.filePath == testUri
            }
        }
    }

    @Test
    fun onDocumentResult_setsNewContentInState() {
        // Given
        createViewModel()
        assertEquals(testContents, viewModel.currentState.items)

        // When
        viewModel.onAction(Action.OnDocumentResult(testUri))

        // Then
        assertTrue {
            viewModel.currentState.items!!.any {
                it.filePath == testUri
            }
        }
        assertTrue {
            val new = viewModel.currentState.items!!.first {
                it.filePath == testUri
            }
            new.name.first().isUpperCase()
        }
    }

    @Test
    fun onDocumentResult_setsNavigationBackInState() {
        // Given
        testAddContent = true
        createViewModel()

        // When
        viewModel.onAction(Action.OnDocumentResult(testUri))

        // Then
        assertEquals(NavigationState.Back, viewModel.currentState.navigationState)
    }

    @Test
    fun onDocumentResult_setsNotAddedContentsMessageInState() {
        // Given
        testAddContent = false
        createViewModel()

        // When
        viewModel.onAction(Action.OnDocumentResult(testUri))

        // Then
        val state = viewModel.state.value
        assertEquals(SnackbarState.NotAddedContentIsExists, state.snackbarState)
    }


    @Test
    fun onDocumentResult_setsVerifyErrorMessageInState() {
        // Given
        val duplicateIdThemesException = DuplicateIdThemesException(
            message = "Foo",
            themes = emptySet(),
        )
        testError = duplicateIdThemesException
        createViewModel()

        // When
        viewModel.onAction(Action.OnDocumentResult(testUri))

        // Then
        assertEquals(
            SnackbarState.VerifyError(duplicateIdThemesException),
            viewModel.currentState.snackbarState,
        )
    }

    @Test
    fun onDocumentResult_setsAddIsFailedMessageInState() {
        // Given
        testError = IOException()
        createViewModel()

        // When
        viewModel.onAction(Action.OnDocumentResult(testUri))

        // Then
        assertEquals(SnackbarState.AddIsFailed, viewModel.currentState.snackbarState)
    }

    @Test
    fun onContentFormatClicked_setsNavigateToContentFormatInState() {
        // Given
        createViewModel()

        // When
        viewModel.onAction(Action.OnContentFormatClicked)

        // Then
        assertEquals(
            NavigationState.NavigateToContentFormat(url = testContentFormatUrl),
            viewModel.currentState.navigationState,
        )
    }

    @Test
    fun onContentFormatClicked_setsContentFormatUrlNotLoadedMessageInState() {
        // Given
        testError = NullPointerException()
        createViewModel()

        // When
        viewModel.onAction(Action.OnContentFormatClicked)

        // Then
        assertEquals(
            SnackbarState.ContentFormatUrlNotLoaded,
            viewModel.currentState.snackbarState,
        )
    }

    private fun buildContent(index: Int, filePath: String? = null, isChecked: Boolean = false) =
        ContentModel(
            id = index.toString(),
            name = "Name $index",
            filePath = filePath ?: "file://data/${index}.txt",
            isChecked = isChecked,
        )
}