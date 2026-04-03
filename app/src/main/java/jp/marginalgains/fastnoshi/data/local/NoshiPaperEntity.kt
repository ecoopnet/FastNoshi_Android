package jp.marginalgains.fastnoshi.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.UUID

@Entity(tableName = "noshi_papers")
@TypeConverters(StringListConverter::class)
data class NoshiPaperEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val templateId: String,
    val omoteGaki: String,
    val names: List<String>,
    val fontId: String,
    val fontSize: Float,
    val paperSize: String,
    val createdAt: Long = System.currentTimeMillis(),
    val lastPrintedAt: Long? = null
)
