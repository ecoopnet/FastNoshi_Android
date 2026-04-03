package jp.marginalgains.fastnoshi.ui.preview

import jp.marginalgains.fastnoshi.domain.model.NoshiFontSet

data class PreviewUiState(
    val templateId: String = "",
    val omoteGaki: String = "",
    val names: List<String> = emptyList(),
    val selectedFontSet: NoshiFontSet = NoshiFontSet.default,
    val omoteGakiFontSize: Float = 28f,
    val nameFontSize: Float = 24f,
    val selectedPaperSize: String = "A4",
    val isColorTemplate: Boolean = true,
    val price: Int = 200,
    val navigateToPrint: NavigateToPrint? = null
)

data class NavigateToPrint(
    val templateId: String,
    val omoteGaki: String,
    val names: List<String>,
    val fontSetId: String,
    val omoteGakiFontSize: Float,
    val nameFontSize: Float,
    val paperSize: String
)
