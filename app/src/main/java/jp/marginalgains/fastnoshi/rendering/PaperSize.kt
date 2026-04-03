package jp.marginalgains.fastnoshi.rendering

/**
 * 印刷用紙サイズ定義（横向き / landscape）
 * 座標系: 72 DPI (1mm = 72/25.4 pt)
 */
enum class PaperSize(
    val widthMm: Float,
    val heightMm: Float,
    val displayName: String,
    val description: String
) {
    A4(297f, 210f, "A4", "小〜中サイズの贈り物に最適"),
    A3(420f, 297f, "A3", "大サイズの贈り物に最適"),
    B4(364f, 257f, "B4", "中〜大サイズの贈り物に最適");

    val widthPt: Float get() = widthMm * MM_TO_PT
    val heightPt: Float get() = heightMm * MM_TO_PT

    companion object {
        private const val MM_TO_PT = 72f / 25.4f

        fun fromString(name: String): PaperSize? =
            entries.find { it.name == name }
    }
}
