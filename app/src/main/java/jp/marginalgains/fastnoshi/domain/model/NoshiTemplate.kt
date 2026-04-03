package jp.marginalgains.fastnoshi.domain.model

data class NoshiTemplate(
    val id: String,
    val name: String,
    val description: String,
    val omoteGakiCandidates: List<String>,
    val pdfFileName: String,
    val pngFileName: String,
    val isColor: Boolean
) {
    companion object {
        val all: List<NoshiTemplate> = listOf(
            NoshiTemplate(
                id = "05_cho_red_on",
                name = "紅白蝶結び（のし付き）",
                description = "繰り返すお祝いに使用",
                omoteGakiCandidates = listOf(
                    "御祝",
                    "御出産祝",
                    "御入学祝",
                    "御新築祝",
                    "御開店祝",
                    "内祝",
                    "御中元",
                    "御歳暮"
                ),
                pdfFileName = "05_cho_red_on.pdf",
                pngFileName = "05_cho_red_on.png",
                isColor = true
            ),
            NoshiTemplate(
                id = "05_musu_red_off",
                name = "紅白結び切り（のしなし）",
                description = "一度きりのお祝いに使用",
                omoteGakiCandidates = listOf(
                    "快気祝",
                    "御見舞御礼",
                    "内祝"
                ),
                pdfFileName = "05_musu_red_off.pdf",
                pngFileName = "05_musu_red_off.png",
                isColor = true
            ),
            NoshiTemplate(
                id = "10_musu_red_on",
                name = "紅白結び切り10本（のし付き）",
                description = "結婚に使用",
                omoteGakiCandidates = listOf(
                    "寿",
                    "御結婚祝",
                    "御結婚内祝"
                ),
                pdfFileName = "10_musu_red_on.pdf",
                pngFileName = "10_musu_red_on.png",
                isColor = true
            ),
            NoshiTemplate(
                id = "05_musu_black_off",
                name = "黒白結び切り",
                description = "弔事に使用",
                omoteGakiCandidates = listOf(
                    "御霊前",
                    "御仏前",
                    "御香典",
                    "志"
                ),
                pdfFileName = "05_musu_black_off.pdf",
                pngFileName = "05_musu_black_off.png",
                isColor = false
            )
        )

        fun findById(id: String): NoshiTemplate? = all.find { it.id == id }
    }
}
