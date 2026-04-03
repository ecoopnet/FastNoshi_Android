package jp.marginalgains.fastnoshi.ui.history

import jp.marginalgains.fastnoshi.domain.model.NoshiPaper

data class HistoryUiState(
    val unprintedPapers: List<NoshiPaper> = emptyList(),
    val printedPapers: List<NoshiPaper> = emptyList()
) {
    val isEmpty: Boolean
        get() = unprintedPapers.isEmpty() && printedPapers.isEmpty()
}
