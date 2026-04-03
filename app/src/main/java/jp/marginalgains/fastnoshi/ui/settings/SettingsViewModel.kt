package jp.marginalgains.fastnoshi.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import jp.marginalgains.fastnoshi.data.repository.PreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        preferencesRepository.defaultFontId,
        preferencesRepository.defaultPaperSize
    ) { fontId, paperSize ->
        SettingsUiState(
            defaultFontId = fontId,
            defaultPaperSize = paperSize
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsUiState()
    )

    fun updateDefaultFont(fontId: String) {
        viewModelScope.launch {
            preferencesRepository.setDefaultFontId(fontId)
        }
    }

    fun updateDefaultPaperSize(paperSize: String) {
        viewModelScope.launch {
            preferencesRepository.setDefaultPaperSize(paperSize)
        }
    }
}
