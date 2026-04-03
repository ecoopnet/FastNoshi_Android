package jp.marginalgains.fastnoshi.ui.print

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import jp.marginalgains.fastnoshi.data.repository.NpsRepository
import jp.marginalgains.fastnoshi.domain.model.NoshiFontSet
import jp.marginalgains.fastnoshi.domain.model.NoshiTemplate
import jp.marginalgains.fastnoshi.domain.model.NpsColorMode
import jp.marginalgains.fastnoshi.rendering.FontResolver
import jp.marginalgains.fastnoshi.rendering.NoshiPdfGenerator
import jp.marginalgains.fastnoshi.rendering.NoshiRenderer
import jp.marginalgains.fastnoshi.rendering.PaperSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

@HiltViewModel
class PrintViewModel @Inject constructor(
    @param:ApplicationContext private val appContext: Context,
    private val npsRepository: NpsRepository
) : ViewModel() {

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

    fun onUploadAndPrint() {
        val current = _uiState.value
        val template = NoshiTemplate.findById(current.templateId) ?: run {
            _uiState.value = current.copy(errorMessage = "テンプレートが見つかりません")
            return
        }
        val paperSize = PaperSize.fromString(current.paperSize) ?: PaperSize.A4
        val fontSet = NoshiFontSet.findById(current.fontSetId) ?: NoshiFontSet.default
        val typeface = FontResolver.resolve(fontSet)
        val colorMode = NpsColorMode.fromTemplate(template)

        _uiState.value = current.copy(isUploading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val pdfBytes = withContext(Dispatchers.Default) {
                    val renderer = NoshiRenderer(appContext)
                    val generator = NoshiPdfGenerator(renderer)
                    generator.generate(
                        template = template,
                        omoteGaki = current.omoteGaki,
                        names = current.names,
                        typeface = typeface,
                        omoteGakiFontSize = current.omoteGakiFontSize,
                        nameFontSize = current.nameFontSize,
                        paperSize = paperSize
                    )
                }

                val fileName = "${template.id}_noshi.pdf"
                val requestBody = pdfBytes.toRequestBody(
                    "application/pdf".toMediaType()
                )
                val filePart = MultipartBody.Part.createFormData(
                    "file",
                    fileName,
                    requestBody
                )

                val uploadResult = npsRepository.upload(
                    file = filePart,
                    paperSize = paperSize.npsCode,
                    colorMode = colorMode.code,
                    fileName = fileName
                )

                uploadResult.fold(
                    onSuccess = { response ->
                        if (response.success && response.printId != null) {
                            val expiresAt = response.accessKey?.let {
                                fetchExpiryDate(it)
                            }
                            _uiState.value = _uiState.value.copy(
                                isUploading = false,
                                printId = response.printId,
                                expiresAt = expiresAt
                            )
                        } else {
                            _uiState.value = _uiState.value.copy(
                                isUploading = false,
                                errorMessage = response.resultCodeMessage
                                    ?: "アップロードに失敗しました"
                            )
                        }
                    },
                    onFailure = { e ->
                        _uiState.value = _uiState.value.copy(
                            isUploading = false,
                            errorMessage = "通信エラー: ${e.message}"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isUploading = false,
                    errorMessage = "PDF生成エラー: ${e.message}"
                )
            }
        }
    }

    private suspend fun fetchExpiryDate(accessKey: String): String? =
        npsRepository.getFileInfo(accessKey).getOrNull()?.expiryDate

    internal fun onUploadStarted() {
        _uiState.value = _uiState.value.copy(
            isUploading = true,
            errorMessage = null
        )
    }

    internal fun onPrintSuccess(printId: String, expiresAt: String?) {
        _uiState.value = _uiState.value.copy(
            isUploading = false,
            printId = printId,
            expiresAt = expiresAt,
            errorMessage = null
        )
    }

    internal fun onPrintError(message: String) {
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
