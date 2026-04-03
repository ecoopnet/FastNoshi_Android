package jp.marginalgains.fastnoshi.ui.navigation

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class NoshiRouteTest {

    @Test
    fun `全ルートが一意である`() {
        val routes = listOf(
            NoshiRoute.Home,
            NoshiRoute.GuidedFlow,
            NoshiRoute.Expert,
            NoshiRoute.ExpertOmoteGaki,
            NoshiRoute.TextInput,
            NoshiRoute.Preview,
            NoshiRoute.Print,
            NoshiRoute.Result,
            NoshiRoute.MannersGuide,
            NoshiRoute.History,
            NoshiRoute.HistoryDetail,
            NoshiRoute.Settings
        )
        val routeStrings = routes.map { it.route }
        assertEquals(routeStrings.size, routeStrings.toSet().size)
    }

    @Test
    fun `全画面分のルートが12個定義されている`() {
        val routes = listOf(
            NoshiRoute.Home,
            NoshiRoute.GuidedFlow,
            NoshiRoute.Expert,
            NoshiRoute.ExpertOmoteGaki,
            NoshiRoute.TextInput,
            NoshiRoute.Preview,
            NoshiRoute.Print,
            NoshiRoute.Result,
            NoshiRoute.MannersGuide,
            NoshiRoute.History,
            NoshiRoute.HistoryDetail,
            NoshiRoute.Settings
        )
        assertEquals(12, routes.size)
    }

    @Test
    fun `TextInputのルートにtemplateIdとomoteGaki引数がある`() {
        assertTrue(NoshiRoute.TextInput.route.contains("{templateId}"))
        assertTrue(NoshiRoute.TextInput.route.contains("{omoteGaki}"))
    }

    @Test
    fun `TextInputのcreateRouteが正しいパスを生成する`() {
        val route = NoshiRoute.TextInput.createRoute("05_cho_red_on", "御祝")
        assertEquals("textInput/05_cho_red_on/御祝", route)
    }

    @Test
    fun `ExpertOmoteGakiのcreateRouteが正しいパスを生成する`() {
        val route = NoshiRoute.ExpertOmoteGaki.createRoute("10_musu_red_on")
        assertEquals("expertOmoteGaki/10_musu_red_on", route)
    }

    @Test
    fun `HistoryDetailのcreateRouteが正しいパスを生成する`() {
        val route = NoshiRoute.HistoryDetail.createRoute("uuid-123")
        assertEquals("historyDetail/uuid-123", route)
    }

    @Test
    fun `Homeがスタート画面である`() {
        assertEquals("home", NoshiRoute.Home.route)
    }
}
