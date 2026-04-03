package jp.marginalgains.fastnoshi.ui.print

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class PrintViewModelTest {

    private lateinit var viewModel: PrintViewModel

    @BeforeEach
    fun setUp() {
        viewModel = PrintViewModel()
        viewModel.init(
            templateId = "05_cho_red_on",
            omoteGaki = "御祝",
            names = listOf("山田太郎"),
            fontSetId = "mincho",
            omoteGakiFontSize = 28f,
            nameFontSize = 24f,
            paperSize = "A4"
        )
    }

    @Test
    fun `初期状態が正しい`() {
        val state = viewModel.uiState.value
        assertEquals("05_cho_red_on", state.templateId)
        assertEquals("御祝", state.omoteGaki)
        assertEquals(listOf("山田太郎"), state.names)
        assertEquals("A4", state.paperSize)
        assertFalse(state.isUploading)
        assertNull(state.printId)
        assertNull(state.errorMessage)
    }

    @Nested
    inner class PrintResult {
        @Test
        fun `印刷成功で予約番号が設定される`() {
            viewModel.onPrintSuccess(
                printId = "ABC12345",
                expiresAt = "2026-04-10 23:59"
            )
            val state = viewModel.uiState.value
            assertEquals("ABC12345", state.printId)
            assertEquals("2026-04-10 23:59", state.expiresAt)
            assertFalse(state.isUploading)
        }

        @Test
        fun `印刷失敗でエラーメッセージが設定される`() {
            viewModel.onPrintError("アップロードに失敗しました")
            val state = viewModel.uiState.value
            assertNull(state.printId)
            assertEquals("アップロードに失敗しました", state.errorMessage)
            assertFalse(state.isUploading)
        }

        @Test
        fun `予約番号をクリップボードにコピーできる`() {
            viewModel.onPrintSuccess(printId = "ABC12345", expiresAt = "2026-04-10 23:59")
            viewModel.onCopyPrintId()
            assertTrue(viewModel.uiState.value.showCopiedFeedback)
        }

        @Test
        fun `コピーフィードバック消費後にfalseに戻る`() {
            viewModel.onPrintSuccess(printId = "ABC12345", expiresAt = "2026-04-10 23:59")
            viewModel.onCopyPrintId()
            viewModel.onCopiedFeedbackConsumed()
            assertFalse(viewModel.uiState.value.showCopiedFeedback)
        }
    }

    @Nested
    inner class UploadState {
        @Test
        fun `アップロード開始で状態が変わる`() {
            viewModel.onUploadStarted()
            assertTrue(viewModel.uiState.value.isUploading)
            assertNull(viewModel.uiState.value.errorMessage)
        }
    }

    @Nested
    inner class Navigation {
        @Test
        fun `ホームに戻るナビゲーション`() {
            viewModel.onPrintSuccess(printId = "ABC12345", expiresAt = "2026-04-10 23:59")
            viewModel.onNavigateHome()
            assertTrue(viewModel.uiState.value.navigateToHome)
        }
    }
}
