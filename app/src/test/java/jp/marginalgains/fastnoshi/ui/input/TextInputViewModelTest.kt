package jp.marginalgains.fastnoshi.ui.input

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TextInputViewModelTest {

    private lateinit var viewModel: TextInputViewModel

    @BeforeEach
    fun setUp() {
        viewModel = TextInputViewModel()
        viewModel.init(templateId = "05_cho_red_on", omoteGaki = "御祝")
    }

    @Test
    fun `初期状態が正しい`() {
        val state = viewModel.uiState.value
        assertEquals("05_cho_red_on", state.templateId)
        assertEquals("御祝", state.omoteGaki)
        assertTrue(state.names.isEmpty())
        assertFalse(state.canProceed)
    }

    @Nested
    inner class NameManagement {
        @Test
        fun `名前を追加できる`() {
            viewModel.onAddName()
            assertEquals(1, viewModel.uiState.value.names.size)
            assertEquals("", viewModel.uiState.value.names[0])
        }

        @Test
        fun `名前を更新できる`() {
            viewModel.onAddName()
            viewModel.onNameChanged(0, "山田太郎")
            assertEquals("山田太郎", viewModel.uiState.value.names[0])
        }

        @Test
        fun `名前を削除できる`() {
            viewModel.onAddName()
            viewModel.onAddName()
            viewModel.onNameChanged(0, "山田")
            viewModel.onNameChanged(1, "鈴木")
            viewModel.onRemoveName(0)
            assertEquals(1, viewModel.uiState.value.names.size)
            assertEquals("鈴木", viewModel.uiState.value.names[0])
        }

        @Test
        fun `最大5名まで追加可能`() {
            repeat(5) { viewModel.onAddName() }
            assertFalse(viewModel.uiState.value.canAddName)
        }

        @Test
        fun `4名以下はまだ追加可能`() {
            repeat(4) { viewModel.onAddName() }
            assertTrue(viewModel.uiState.value.canAddName)
        }
    }

    @Nested
    inner class Validation {
        @Test
        fun `名前が1つ以上入力されていればcanProceedがtrue`() {
            viewModel.onAddName()
            viewModel.onNameChanged(0, "山田太郎")
            assertTrue(viewModel.uiState.value.canProceed)
        }

        @Test
        fun `空文字の名前のみではcanProceedがfalse`() {
            viewModel.onAddName()
            viewModel.onNameChanged(0, "")
            assertFalse(viewModel.uiState.value.canProceed)
        }

        @Test
        fun `名前がない場合canProceedがfalse`() {
            assertFalse(viewModel.uiState.value.canProceed)
        }

        @Test
        fun `表書きが空の場合canProceedがfalse`() {
            viewModel.init(templateId = "05_cho_red_on", omoteGaki = "")
            viewModel.onAddName()
            viewModel.onNameChanged(0, "山田")
            assertFalse(viewModel.uiState.value.canProceed)
        }
    }

    @Nested
    inner class OmoteGakiEdit {
        @Test
        fun `表書きを変更できる`() {
            viewModel.onOmoteGakiChanged("寿")
            assertEquals("寿", viewModel.uiState.value.omoteGaki)
        }
    }
}
