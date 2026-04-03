package jp.marginalgains.fastnoshi.ui.print

data class PrintUiState(
    val templateId: String = "",
    val omoteGaki: String = "",
    val names: List<String> = emptyList(),
    val fontSetId: String = "mincho",
    val omoteGakiFontSize: Float = 28f,
    val nameFontSize: Float = 24f,
    val paperSize: String = "A4",
    val isUploading: Boolean = false,
    val printId: String? = null,
    val expiresAt: String? = null,
    val errorMessage: String? = null,
    val showCopiedFeedback: Boolean = false,
    val navigateToHome: Boolean = false
)
