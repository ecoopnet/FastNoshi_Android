package jp.marginalgains.fastnoshi.ui.history

import app.cash.turbine.test
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import jp.marginalgains.fastnoshi.data.repository.NoshiRepository
import jp.marginalgains.fastnoshi.domain.model.NoshiPaper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: NoshiRepository
    private lateinit var viewModel: HistoryViewModel

    private val unprintedPaper = NoshiPaper(
        id = "unprinted-1",
        templateId = "05_cho_red_on",
        omoteGaki = "御祝",
        names = listOf("山田太郎"),
        fontId = "mincho",
        fontSize = 24f,
        paperSize = "A4",
        createdAt = 1000L,
        lastPrintedAt = null
    )

    private val printedPaper = NoshiPaper(
        id = "printed-1",
        templateId = "05_musu_black_off",
        omoteGaki = "御霊前",
        names = listOf("鈴木花子"),
        fontId = "mincho",
        fontSize = 24f,
        paperSize = "B4",
        createdAt = 2000L,
        lastPrintedAt = 3000L
    )

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        every { repository.getUnprintedPapers() } returns flowOf(listOf(unprintedPaper))
        every { repository.getPrintedPapers() } returns flowOf(listOf(printedPaper))
        viewModel = HistoryViewModel(repository)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `初期状態で未印刷と印刷済みの履歴を取得する`() = runTest {
        viewModel.uiState.test {
            val initial = awaitItem()
            assertTrue(initial.unprintedPapers.isEmpty())
            assertTrue(initial.printedPapers.isEmpty())

            val loaded = awaitItem()
            assertEquals(1, loaded.unprintedPapers.size)
            assertEquals("unprinted-1", loaded.unprintedPapers[0].id)
            assertEquals(1, loaded.printedPapers.size)
            assertEquals("printed-1", loaded.printedPapers[0].id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `未印刷リストが空の場合も正しく表示する`() = runTest {
        every { repository.getUnprintedPapers() } returns flowOf(emptyList())
        viewModel = HistoryViewModel(repository)

        viewModel.uiState.test {
            awaitItem() // initial
            val loaded = awaitItem()
            assertTrue(loaded.unprintedPapers.isEmpty())
            assertEquals(1, loaded.printedPapers.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `印刷済みリストが空の場合も正しく表示する`() = runTest {
        every { repository.getPrintedPapers() } returns flowOf(emptyList())
        viewModel = HistoryViewModel(repository)

        viewModel.uiState.test {
            awaitItem() // initial
            val loaded = awaitItem()
            assertEquals(1, loaded.unprintedPapers.size)
            assertTrue(loaded.printedPapers.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `deletePaper でリポジトリの deletePaper が呼ばれる`() = runTest {
        viewModel.deletePaper(unprintedPaper)
        advanceUntilIdle()

        coVerify { repository.deletePaper(unprintedPaper) }
    }

    @Test
    fun `両方空の場合は空状態になる`() = runTest {
        every { repository.getUnprintedPapers() } returns flowOf(emptyList())
        every { repository.getPrintedPapers() } returns flowOf(emptyList())
        viewModel = HistoryViewModel(repository)

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.unprintedPapers.isEmpty())
            assertTrue(state.printedPapers.isEmpty())
            assertTrue(state.isEmpty)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
