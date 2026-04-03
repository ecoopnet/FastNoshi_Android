package jp.marginalgains.fastnoshi.ui.input

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TextInputViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TextInputUiState())
    val uiState: StateFlow<TextInputUiState> = _uiState.asStateFlow()

    fun init(templateId: String, omoteGaki: String) {
        _uiState.value = TextInputUiState(
            templateId = templateId,
            omoteGaki = omoteGaki
        )
    }

    fun onAddName() {
        val current = _uiState.value
        if (current.names.size >= MAX_NAMES) return
        updateState(current.copy(names = current.names + ""))
    }

    fun onNameChanged(index: Int, value: String) {
        val current = _uiState.value
        val newNames = current.names.toMutableList()
        newNames[index] = value
        updateState(current.copy(names = newNames))
    }

    fun onRemoveName(index: Int) {
        val current = _uiState.value
        val newNames = current.names.toMutableList()
        newNames.removeAt(index)
        updateState(current.copy(names = newNames))
    }

    fun onOmoteGakiChanged(value: String) {
        val current = _uiState.value
        updateState(current.copy(omoteGaki = value))
    }

    fun onProceed() {
        val current = _uiState.value
        if (!current.canProceed) return
        val validNames = current.names.filter { it.trim().isNotEmpty() }
        _uiState.value = current.copy(
            navigateToPreview = NavigateToPreview(
                templateId = current.templateId,
                omoteGaki = current.omoteGaki,
                names = validNames
            )
        )
    }

    fun onNavigationConsumed() {
        _uiState.value = _uiState.value.copy(navigateToPreview = null)
    }

    private fun updateState(state: TextInputUiState) {
        val hasValidName = state.names.any { it.trim().isNotEmpty() }
        val hasValidOmoteGaki = state.omoteGaki.trim().isNotEmpty()
        _uiState.value = state.copy(
            canProceed = hasValidName && hasValidOmoteGaki,
            canAddName = state.names.size < MAX_NAMES
        )
    }

    companion object {
        const val MAX_NAMES = 5
    }
}
