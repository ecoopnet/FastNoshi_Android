package jp.marginalgains.fastnoshi.ui.expert

import jp.marginalgains.fastnoshi.domain.model.NoshiTemplate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ExpertViewModelTest {

    private lateinit var viewModel: ExpertViewModel

    @BeforeEach
    fun setUp() {
        viewModel = ExpertViewModel()
    }

    @Test
    fun `全4テンプレートが取得できる`() {
        assertEquals(4, viewModel.uiState.value.templates.size)
    }

    @Test
    fun `テンプレート選択でナビゲーションイベント発行`() {
        viewModel.onTemplateSelected("05_cho_red_on")
        assertEquals("05_cho_red_on", viewModel.uiState.value.navigateToOmoteGaki)
    }

    @Test
    fun `ナビゲーション消費後にnullに戻る`() {
        viewModel.onTemplateSelected("05_cho_red_on")
        viewModel.onNavigationConsumed()
        assertNull(viewModel.uiState.value.navigateToOmoteGaki)
    }

    @Test
    fun `テンプレートの表書き候補が取得できる`() {
        val template = NoshiTemplate.findById("05_cho_red_on")!!
        assertEquals(8, template.omoteGakiCandidates.size)
    }
}
