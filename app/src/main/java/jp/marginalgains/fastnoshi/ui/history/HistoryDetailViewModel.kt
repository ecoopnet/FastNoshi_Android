package jp.marginalgains.fastnoshi.ui.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import jp.marginalgains.fastnoshi.data.repository.NoshiRepository
import jp.marginalgains.fastnoshi.domain.model.NoshiPaper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HistoryDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repository: NoshiRepository
) : ViewModel() {

    private val paperId: String = checkNotNull(savedStateHandle["paperId"])

    val paper: StateFlow<NoshiPaper?> = repository.getPaperById(paperId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )
}
