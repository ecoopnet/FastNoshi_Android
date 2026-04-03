package jp.marginalgains.fastnoshi.ui.preview

import jp.marginalgains.fastnoshi.domain.model.NoshiFontSet
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class PreviewViewModelTest {

    private lateinit var viewModel: PreviewViewModel

    @BeforeEach
    fun setUp() {
        viewModel = PreviewViewModel()
        viewModel.init(
            templateId = "05_cho_red_on",
            omoteGaki = "御祝",
            names = listOf("山田太郎")
        )
    }

    @Test
    fun `初期状態が正しい`() {
        val state = viewModel.uiState.value
        assertEquals("05_cho_red_on", state.templateId)
        assertEquals("御祝", state.omoteGaki)
        assertEquals(listOf("山田太郎"), state.names)
        assertEquals(NoshiFontSet.default, state.selectedFontSet)
        assertEquals(28f, state.omoteGakiFontSize)
        assertEquals(24f, state.nameFontSize)
        assertEquals("A4", state.selectedPaperSize)
    }

    @Nested
    inner class FontSelection {
        @Test
        fun `フォントを変更できる`() {
            val gothic = NoshiFontSet.findById("gothic")!!
            viewModel.onFontSetChanged(gothic)
            assertEquals(gothic, viewModel.uiState.value.selectedFontSet)
        }

        @Test
        fun `表書きフォントサイズを変更できる`() {
            viewModel.onOmoteGakiFontSizeChanged(36f)
            assertEquals(36f, viewModel.uiState.value.omoteGakiFontSize)
        }

        @Test
        fun `名前フォントサイズを変更できる`() {
            viewModel.onNameFontSizeChanged(20f)
            assertEquals(20f, viewModel.uiState.value.nameFontSize)
        }
    }

    @Nested
    inner class PaperSize {
        @Test
        fun `用紙サイズを変更できる`() {
            viewModel.onPaperSizeChanged("A3")
            assertEquals("A3", viewModel.uiState.value.selectedPaperSize)
        }

        @Test
        fun `B4に変更できる`() {
            viewModel.onPaperSizeChanged("B4")
            assertEquals("B4", viewModel.uiState.value.selectedPaperSize)
        }
    }

    @Nested
    inner class PriceInfo {
        @Test
        fun `カラーテンプレートは200円`() {
            val state = viewModel.uiState.value
            assertTrue(state.isColorTemplate)
            assertEquals(200, state.price)
        }

        @Test
        fun `白黒テンプレートは100円`() {
            viewModel.init(
                templateId = "05_musu_black_off",
                omoteGaki = "御霊前",
                names = listOf("山田太郎")
            )
            val state = viewModel.uiState.value
            assertFalse(state.isColorTemplate)
            assertEquals(100, state.price)
        }
    }

    @Nested
    inner class Navigation {
        @Test
        fun `印刷予約送信でナビゲーションイベント発行`() {
            viewModel.onRequestPrint()
            val nav = viewModel.uiState.value.navigateToPrint
            assertTrue(nav != null)
            assertEquals("05_cho_red_on", nav?.templateId)
            assertEquals("御祝", nav?.omoteGaki)
            assertEquals(listOf("山田太郎"), nav?.names)
        }

        @Test
        fun `ナビゲーション消費後にnullに戻る`() {
            viewModel.onRequestPrint()
            viewModel.onNavigationConsumed()
            assertNull(viewModel.uiState.value.navigateToPrint)
        }
    }
}
