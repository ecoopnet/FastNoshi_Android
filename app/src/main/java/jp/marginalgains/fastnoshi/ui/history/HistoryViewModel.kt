package jp.marginalgains.fastnoshi.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import jp.marginalgains.fastnoshi.data.repository.NoshiRepository
import jp.marginalgains.fastnoshi.domain.model.NoshiPaper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: NoshiRepository
) : ViewModel() {

    val uiState: StateFlow<HistoryUiState> = combine(
        repository.getUnprintedPapers(),
        repository.getPrintedPapers()
    ) { unprinted, printed ->
        HistoryUiState(
            unprintedPapers = unprinted,
            printedPapers = printed
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HistoryUiState()
    )

    fun deletePaper(paper: NoshiPaper) {
        viewModelScope.launch {
            repository.deletePaper(paper)
        }
    }
}
