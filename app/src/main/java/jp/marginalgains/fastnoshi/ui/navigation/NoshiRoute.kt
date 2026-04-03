package jp.marginalgains.fastnoshi.ui.navigation

import android.net.Uri

sealed class NoshiRoute(val route: String) {
    data object Home : NoshiRoute("home")
    data object GuidedFlow : NoshiRoute("guidedFlow")
    data object Expert : NoshiRoute("expert")
    data object ExpertOmoteGaki : NoshiRoute("expertOmoteGaki/{templateId}") {
        fun createRoute(templateId: String) = "expertOmoteGaki/${Uri.encode(templateId)}"
    }
    data object TextInput : NoshiRoute("textInput/{templateId}/{omoteGaki}") {
        fun createRoute(templateId: String, omoteGaki: String) =
            "textInput/${Uri.encode(templateId)}/${Uri.encode(omoteGaki)}"
    }
    data object Preview : NoshiRoute("preview/{templateId}/{omoteGaki}/{names}") {
        fun createRoute(templateId: String, omoteGaki: String, names: List<String>): String {
            val namesParam = names.joinToString(",")
            return "preview/${Uri.encode(templateId)}/${Uri.encode(omoteGaki)}/${Uri.encode(namesParam)}"
        }
    }
    data object Print : NoshiRoute(
        "print/{templateId}/{omoteGaki}/{names}/{fontSetId}/{omoteGakiFontSize}/{nameFontSize}/{paperSize}"
    ) {
        fun createRoute(
            templateId: String,
            omoteGaki: String,
            names: List<String>,
            fontSetId: String,
            omoteGakiFontSize: Float,
            nameFontSize: Float,
            paperSize: String
        ): String {
            val namesParam = names.joinToString(",")
            return "print/${Uri.encode(templateId)}/${Uri.encode(omoteGaki)}/${Uri.encode(namesParam)}/" +
                "${Uri.encode(fontSetId)}/$omoteGakiFontSize/$nameFontSize/${Uri.encode(paperSize)}"
        }
    }
    data object MannersGuide : NoshiRoute("mannersGuide")
    data object History : NoshiRoute("history")
    data object HistoryDetail : NoshiRoute("historyDetail/{paperId}") {
        fun createRoute(paperId: String) = "historyDetail/${Uri.encode(paperId)}"
    }
    data object Settings : NoshiRoute("settings")
}
