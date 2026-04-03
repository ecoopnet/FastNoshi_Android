package jp.marginalgains.fastnoshi.ui.guidedflow

import jp.marginalgains.fastnoshi.domain.model.GuidedFlowStep
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class GuidedFlowViewModelTest {

    private lateinit var viewModel: GuidedFlowViewModel

    @BeforeEach
    fun setUp() {
        viewModel = GuidedFlowViewModel()
    }

    @Test
    fun `初期状態のUIステートが正しい`() {
        val state = viewModel.uiState.value
        assertEquals(GuidedFlowStep.PURPOSE, state.currentStep)
        assertEquals("どのようなご用途ですか？", state.questionText)
        assertEquals(3, state.choices.size)
        assertFalse(state.canGoBack)
        assertNull(state.navigateToTextInput)
    }

    @Test
    fun `選択肢をタップするとステートが更新される`() {
        viewModel.onChoiceSelected(0) // お祝い
        val state = viewModel.uiState.value
        assertEquals(GuidedFlowStep.CELEBRATION_REPEAT, state.currentStep)
        assertEquals("何度あってもよいお祝いですか？", state.questionText)
        assertTrue(state.canGoBack)
    }

    @Nested
    inner class NavigationToTextInput {
        @Test
        fun `OMOTE_GAKI_SELECTIONで表書き選択するとナビゲーションイベント発行`() {
            viewModel.onChoiceSelected(0) // お祝い
            viewModel.onChoiceSelected(0) // はい → OMOTE_GAKI_SELECTION
            val state = viewModel.uiState.value
            assertEquals(GuidedFlowStep.OMOTE_GAKI_SELECTION, state.currentStep)
            assertTrue(state.omoteGakiCandidates.isNotEmpty())

            viewModel.onOmoteGakiSelected("御祝")
            val updatedState = viewModel.uiState.value
            assertEquals("05_cho_red_on", updatedState.navigateToTextInput?.templateId)
            assertEquals("御祝", updatedState.navigateToTextInput?.omoteGaki)
        }

        @Test
        fun `ナビゲーション消費後にnullに戻る`() {
            viewModel.onChoiceSelected(0) // お祝い
            viewModel.onChoiceSelected(0) // はい
            viewModel.onOmoteGakiSelected("御祝")
            viewModel.onNavigationConsumed()
            assertNull(viewModel.uiState.value.navigateToTextInput)
        }
    }

    @Nested
    inner class GoBack {
        @Test
        fun `戻るでステートが復元される`() {
            viewModel.onChoiceSelected(0) // お祝い
            viewModel.onGoBack()
            val state = viewModel.uiState.value
            assertEquals(GuidedFlowStep.PURPOSE, state.currentStep)
            assertFalse(state.canGoBack)
        }

        @Test
        fun `表書き選択から戻ると候補がクリアされる`() {
            viewModel.onChoiceSelected(0) // お祝い
            viewModel.onChoiceSelected(0) // はい → OMOTE_GAKI_SELECTION
            assertTrue(viewModel.uiState.value.omoteGakiCandidates.isNotEmpty())
            viewModel.onGoBack()
            assertTrue(viewModel.uiState.value.omoteGakiCandidates.isEmpty())
        }
    }

    @Nested
    inner class StepProgress {
        @Test
        fun `PURPOSE のステップインデックスは0`() {
            assertEquals(0, viewModel.uiState.value.stepIndex)
        }

        @Test
        fun `CELEBRATION_REPEAT のステップインデックスは1`() {
            viewModel.onChoiceSelected(0) // お祝い
            assertEquals(1, viewModel.uiState.value.stepIndex)
        }

        @Test
        fun `OMOTE_GAKI_SELECTION のステップインデックスが最大`() {
            viewModel.onChoiceSelected(0) // お祝い
            viewModel.onChoiceSelected(0) // はい
            val state = viewModel.uiState.value
            assertEquals(GuidedFlowStep.OMOTE_GAKI_SELECTION, state.currentStep)
            assertTrue(state.stepIndex > 0)
        }
    }
}
