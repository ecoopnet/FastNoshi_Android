package jp.marginalgains.fastnoshi.ui.preview

import androidx.lifecycle.ViewModel
import jp.marginalgains.fastnoshi.domain.model.NoshiFontSet
import jp.marginalgains.fastnoshi.domain.model.NoshiTemplate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreviewViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PreviewUiState())
    val uiState: StateFlow<PreviewUiState> = _uiState.asStateFlow()

    fun init(templateId: String, omoteGaki: String, names: List<String>) {
        val template = NoshiTemplate.findById(templateId)
        val isColor = template?.isColor ?: true
        _uiState.value = PreviewUiState(
            templateId = templateId,
            omoteGaki = omoteGaki,
            names = names,
            isColorTemplate = isColor,
            price = if (isColor) PRICE_COLOR else PRICE_MONO
        )
    }

    fun onFontSetChanged(fontSet: NoshiFontSet) {
        _uiState.value = _uiState.value.copy(selectedFontSet = fontSet)
    }

    fun onOmoteGakiFontSizeChanged(size: Float) {
        _uiState.value = _uiState.value.copy(omoteGakiFontSize = size)
    }

    fun onNameFontSizeChanged(size: Float) {
        _uiState.value = _uiState.value.copy(nameFontSize = size)
    }

    fun onPaperSizeChanged(paperSize: String) {
        _uiState.value = _uiState.value.copy(selectedPaperSize = paperSize)
    }

    fun onRequestPrint() {
        val current = _uiState.value
        _uiState.value = current.copy(
            navigateToPrint = NavigateToPrint(
                templateId = current.templateId,
                omoteGaki = current.omoteGaki,
                names = current.names,
                fontSetId = current.selectedFontSet.id,
                omoteGakiFontSize = current.omoteGakiFontSize,
                nameFontSize = current.nameFontSize,
                paperSize = current.selectedPaperSize
            )
        )
    }

    fun onNavigationConsumed() {
        _uiState.value = _uiState.value.copy(navigateToPrint = null)
    }

    companion object {
        const val PRICE_COLOR = 200
        const val PRICE_MONO = 100
    }
}
