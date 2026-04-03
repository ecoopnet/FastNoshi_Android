package jp.marginalgains.fastnoshi.data.repository

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import jp.marginalgains.fastnoshi.data.local.NoshiPaperDao
import jp.marginalgains.fastnoshi.data.local.NoshiPaperEntity
import jp.marginalgains.fastnoshi.domain.model.NoshiPaper
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class NoshiRepositoryTest {

    private lateinit var dao: NoshiPaperDao
    private lateinit var repository: NoshiRepository

    private val testEntity = NoshiPaperEntity(
        id = "test-id",
        templateId = "05_cho_red_on",
        omoteGaki = "御祝",
        names = listOf("山田太郎"),
        fontId = "mincho",
        fontSize = 24f,
        paperSize = "A4",
        createdAt = 1000L,
        lastPrintedAt = null
    )

    private val printedEntity = testEntity.copy(
        id = "printed-id",
        lastPrintedAt = 2000L
    )

    @BeforeEach
    fun setup() {
        dao = mockk(relaxed = true)
        repository = NoshiRepository(dao)
    }

    @Test
    fun `getAllPapers は Entity を NoshiPaper に変換して返す`() = runTest {
        every { dao.getAllPapers() } returns flowOf(listOf(testEntity))

        repository.getAllPapers().test {
            val papers = awaitItem()
            assertEquals(1, papers.size)
            assertEquals("test-id", papers[0].id)
            assertEquals("御祝", papers[0].omoteGaki)
            assertEquals(listOf("山田太郎"), papers[0].names)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getPaperById は該当する NoshiPaper を返す`() = runTest {
        every { dao.getPaperById("test-id") } returns flowOf(testEntity)

        repository.getPaperById("test-id").test {
            val paper = awaitItem()
            assertEquals("test-id", paper?.id)
            assertEquals("05_cho_red_on", paper?.templateId)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getPaperById は存在しないIDでnullを返す`() = runTest {
        every { dao.getPaperById("unknown") } returns flowOf(null)

        repository.getPaperById("unknown").test {
            assertNull(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getUnprintedPapers は未印刷のみ返す`() = runTest {
        every { dao.getUnprintedPapers() } returns flowOf(listOf(testEntity))

        repository.getUnprintedPapers().test {
            val papers = awaitItem()
            assertEquals(1, papers.size)
            assertNull(papers[0].lastPrintedAt)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getPrintedPapers は印刷済みのみ返す`() = runTest {
        every { dao.getPrintedPapers() } returns flowOf(listOf(printedEntity))

        repository.getPrintedPapers().test {
            val papers = awaitItem()
            assertEquals(1, papers.size)
            assertEquals(2000L, papers[0].lastPrintedAt)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `insertPaper は NoshiPaper を Entity に変換して DAO に渡す`() = runTest {
        val paper = NoshiPaper(
            id = "new-id",
            templateId = "05_cho_red_on",
            omoteGaki = "御祝",
            names = listOf("山田太郎"),
            fontId = "mincho",
            fontSize = 24f,
            paperSize = "A4",
            createdAt = 1000L
        )

        val slot = slot<NoshiPaperEntity>()
        coEvery { dao.insertPaper(capture(slot)) } returns Unit

        repository.insertPaper(paper)

        coVerify { dao.insertPaper(any()) }
        assertEquals("new-id", slot.captured.id)
        assertEquals("御祝", slot.captured.omoteGaki)
    }

    @Test
    fun `deletePaper は NoshiPaper を Entity に変換して削除する`() = runTest {
        val paper = NoshiPaper(
            id = "test-id",
            templateId = "05_cho_red_on",
            omoteGaki = "御祝",
            names = listOf("山田太郎"),
            fontId = "mincho",
            fontSize = 24f,
            paperSize = "A4",
            createdAt = 1000L
        )

        repository.deletePaper(paper)

        coVerify { dao.deletePaper(match { it.id == "test-id" }) }
    }

    @Test
    fun `updatePaper は NoshiPaper を Entity に変換して更新する`() = runTest {
        val paper = NoshiPaper(
            id = "test-id",
            templateId = "05_cho_red_on",
            omoteGaki = "寿",
            names = listOf("山田太郎", "鈴木花子"),
            fontId = "gothic",
            fontSize = 28f,
            paperSize = "B4",
            createdAt = 1000L,
            lastPrintedAt = 3000L
        )

        val slot = slot<NoshiPaperEntity>()
        coEvery { dao.updatePaper(capture(slot)) } returns Unit

        repository.updatePaper(paper)

        coVerify { dao.updatePaper(any()) }
        assertEquals("寿", slot.captured.omoteGaki)
        assertEquals("gothic", slot.captured.fontId)
        assertEquals(3000L, slot.captured.lastPrintedAt)
    }

    @Test
    fun `Entity から NoshiPaper への変換が全フィールド正しい`() = runTest {
        every { dao.getAllPapers() } returns flowOf(listOf(testEntity))

        repository.getAllPapers().test {
            val paper = awaitItem()[0]
            assertEquals(testEntity.id, paper.id)
            assertEquals(testEntity.templateId, paper.templateId)
            assertEquals(testEntity.omoteGaki, paper.omoteGaki)
            assertEquals(testEntity.names, paper.names)
            assertEquals(testEntity.fontId, paper.fontId)
            assertEquals(testEntity.fontSize, paper.fontSize)
            assertEquals(testEntity.paperSize, paper.paperSize)
            assertEquals(testEntity.createdAt, paper.createdAt)
            assertEquals(testEntity.lastPrintedAt, paper.lastPrintedAt)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `空リストの場合は空リストを返す`() = runTest {
        every { dao.getAllPapers() } returns flowOf(emptyList())

        repository.getAllPapers().test {
            assertTrue(awaitItem().isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
