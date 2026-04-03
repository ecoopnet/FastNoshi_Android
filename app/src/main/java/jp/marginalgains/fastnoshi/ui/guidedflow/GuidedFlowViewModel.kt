package jp.marginalgains.fastnoshi.ui.guidedflow

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import jp.marginalgains.fastnoshi.domain.model.GuidedFlowEngine
import jp.marginalgains.fastnoshi.domain.model.GuidedFlowStep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class GuidedFlowViewModel @Inject constructor() : ViewModel() {

    private val engine = GuidedFlowEngine()

    private val _uiState = MutableStateFlow(buildUiState())
    val uiState: StateFlow<GuidedFlowUiState> = _uiState.asStateFlow()

    fun onChoiceSelected(index: Int) {
        engine.select(index)
        _uiState.value = buildUiState()
    }

    fun onOmoteGakiSelected(omoteGaki: String) {
        val templateId = engine.selectedTemplateId ?: return
        _uiState.value = _uiState.value.copy(
            navigateToTextInput = NavigateToTextInput(templateId, omoteGaki)
        )
    }

    fun onGoBack() {
        if (engine.canGoBack) {
            engine.goBack()
            _uiState.value = buildUiState()
        }
    }

    fun onNavigationConsumed() {
        _uiState.value = _uiState.value.copy(navigateToTextInput = null)
    }

    private fun buildUiState(): GuidedFlowUiState = GuidedFlowUiState(
        currentStep = engine.currentStep,
        questionText = engine.questionText,
        choices = engine.availableChoices,
        omoteGakiCandidates = engine.omoteGakiCandidates,
        canGoBack = engine.canGoBack,
        stepIndex = stepIndexOf(engine.currentStep)
    )

    private fun stepIndexOf(step: GuidedFlowStep): Int = when (step) {
        GuidedFlowStep.PURPOSE -> 0

        GuidedFlowStep.CELEBRATION_REPEAT,
        GuidedFlowStep.CONDOLENCE_TYPE,
        GuidedFlowStep.VISIT_TYPE -> 1

        GuidedFlowStep.MARRIAGE_CHECK -> 2

        GuidedFlowStep.OMOTE_GAKI_SELECTION -> 3
    }
}
