package jp.marginalgains.fastnoshi.domain.model

data class NoshiFontSet(
    val id: String,
    val displayName: String,
    val fontFamily: String,
    val isBold: Boolean = false
) {
    companion object {
        private val mincho = NoshiFontSet(
            id = "mincho",
            displayName = "明朝体",
            fontFamily = "Noto Serif JP"
        )

        private val minchoBold = NoshiFontSet(
            id = "mincho_bold",
            displayName = "明朝体（太字）",
            fontFamily = "Noto Serif JP",
            isBold = true
        )

        private val gothic = NoshiFontSet(
            id = "gothic",
            displayName = "ゴシック体",
            fontFamily = "Noto Sans JP"
        )

        val all: List<NoshiFontSet> = listOf(mincho, minchoBold, gothic)

        val default: NoshiFontSet = mincho

        fun findById(id: String): NoshiFontSet? = all.find { it.id == id }
    }
}
