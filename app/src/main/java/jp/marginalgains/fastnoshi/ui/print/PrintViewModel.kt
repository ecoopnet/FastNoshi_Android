package jp.marginalgains.fastnoshi.ui.print

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PrintViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PrintUiState())
    val uiState: StateFlow<PrintUiState> = _uiState.asStateFlow()

    fun init(
        templateId: String,
        omoteGaki: String,
        names: List<String>,
        fontSetId: String,
        omoteGakiFontSize: Float,
        nameFontSize: Float,
        paperSize: String
    ) {
        _uiState.value = PrintUiState(
            templateId = templateId,
            omoteGaki = omoteGaki,
            names = names,
            fontSetId = fontSetId,
            omoteGakiFontSize = omoteGakiFontSize,
            nameFontSize = nameFontSize,
            paperSize = paperSize
        )
    }

    fun onUploadStarted() {
        _uiState.value = _uiState.value.copy(
            isUploading = true,
            errorMessage = null
        )
    }

    fun onPrintSuccess(printId: String, expiresAt: String) {
        _uiState.value = _uiState.value.copy(
            isUploading = false,
            printId = printId,
            expiresAt = expiresAt,
            errorMessage = null
        )
    }

    fun onPrintError(message: String) {
        _uiState.value = _uiState.value.copy(
            isUploading = false,
            errorMessage = message
        )
    }

    fun onCopyPrintId() {
        _uiState.value = _uiState.value.copy(showCopiedFeedback = true)
    }

    fun onCopiedFeedbackConsumed() {
        _uiState.value = _uiState.value.copy(showCopiedFeedback = false)
    }

    fun onNavigateHome() {
        _uiState.value = _uiState.value.copy(navigateToHome = true)
    }
}
