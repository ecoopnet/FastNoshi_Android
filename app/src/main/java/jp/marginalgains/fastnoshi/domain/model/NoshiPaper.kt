package jp.marginalgains.fastnoshi.domain.model

import java.util.UUID

data class NoshiPaper(
    val id: String = UUID.randomUUID().toString(),
    val templateId: String,
    val omoteGaki: String,
    val names: List<String>,
    val fontId: String,
    val fontSize: Float,
    val paperSize: String,
    val createdAt: Long = System.currentTimeMillis(),
    val lastPrintedAt: Long? = null
) {
    val template: NoshiTemplate?
        get() = NoshiTemplate.findById(templateId)

    val isPrinted: Boolean
        get() = lastPrintedAt != null
}
