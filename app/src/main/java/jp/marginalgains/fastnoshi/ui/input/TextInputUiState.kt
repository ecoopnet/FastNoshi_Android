package jp.marginalgains.fastnoshi.ui.input

data class TextInputUiState(
    val templateId: String = "",
    val omoteGaki: String = "",
    val names: List<String> = emptyList(),
    val canProceed: Boolean = false,
    val canAddName: Boolean = true,
    val navigateToPreview: NavigateToPreview? = null
)

data class NavigateToPreview(val templateId: String, val omoteGaki: String, val names: List<String>)
