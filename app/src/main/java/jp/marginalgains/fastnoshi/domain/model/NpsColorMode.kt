package jp.marginalgains.fastnoshi.domain.model

/**
 * NPS APIのカラーモードコード値
 * iOS版 ColorMode enum に対応
 */
enum class NpsColorMode(val code: String) {
    COLOR("1"),
    MONOCHROME("2");

    companion object {
        fun fromTemplate(template: NoshiTemplate): NpsColorMode =
            if (template.isColor) COLOR else MONOCHROME
    }
}
