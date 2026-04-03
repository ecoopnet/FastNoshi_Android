package jp.marginalgains.fastnoshi.ui.guidedflow

import jp.marginalgains.fastnoshi.domain.model.FlowChoice
import jp.marginalgains.fastnoshi.domain.model.GuidedFlowStep

data class GuidedFlowUiState(
    val currentStep: GuidedFlowStep = GuidedFlowStep.PURPOSE,
    val questionText: String = "",
    val choices: List<FlowChoice> = emptyList(),
    val omoteGakiCandidates: List<String> = emptyList(),
    val canGoBack: Boolean = false,
    val stepIndex: Int = 0,
    val navigateToTextInput: NavigateToTextInput? = null
)

data class NavigateToTextInput(val templateId: String, val omoteGaki: String)
