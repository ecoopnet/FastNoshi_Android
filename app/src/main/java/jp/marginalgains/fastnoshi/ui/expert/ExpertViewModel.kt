package jp.marginalgains.fastnoshi.ui.expert

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class ExpertViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ExpertUiState())
    val uiState: StateFlow<ExpertUiState> = _uiState.asStateFlow()

    fun onTemplateSelected(templateId: String) {
        _uiState.value = _uiState.value.copy(navigateToOmoteGaki = templateId)
    }

    fun onNavigationConsumed() {
        _uiState.value = _uiState.value.copy(navigateToOmoteGaki = null)
    }
}
