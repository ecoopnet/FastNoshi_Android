package jp.marginalgains.fastnoshi.ui.navigation

sealed class NoshiRoute(val route: String) {
    data object Home : NoshiRoute("home")
    data object GuidedFlow : NoshiRoute("guidedFlow")
    data object Expert : NoshiRoute("expert")
    data object ExpertOmoteGaki : NoshiRoute("expertOmoteGaki/{templateId}") {
        fun createRoute(templateId: String) = "expertOmoteGaki/$templateId"
    }
    data object TextInput : NoshiRoute("textInput/{templateId}/{omoteGaki}") {
        fun createRoute(templateId: String, omoteGaki: String) = "textInput/$templateId/$omoteGaki"
    }
    data object Preview : NoshiRoute("preview")
    data object Print : NoshiRoute("print")
    data object Result : NoshiRoute("result")
    data object MannersGuide : NoshiRoute("mannersGuide")
    data object History : NoshiRoute("history")
    data object HistoryDetail : NoshiRoute("historyDetail/{paperId}") {
        fun createRoute(paperId: String) = "historyDetail/$paperId"
    }
    data object Settings : NoshiRoute("settings")
}
