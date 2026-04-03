package jp.marginalgains.fastnoshi.ui.navigation

import android.net.Uri
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class NoshiRouteTest {

    private val allRoutes = listOf(
        NoshiRoute.Home,
        NoshiRoute.GuidedFlow,
        NoshiRoute.Expert,
        NoshiRoute.ExpertOmoteGaki,
        NoshiRoute.TextInput,
        NoshiRoute.Preview,
        NoshiRoute.Print,
        NoshiRoute.MannersGuide,
        NoshiRoute.History,
        NoshiRoute.HistoryDetail,
        NoshiRoute.Settings,
        NoshiRoute.PrintGuide,
        NoshiRoute.PrintInfo
    )

    @BeforeEach
    fun setUp() {
        mockkStatic(Uri::class)
        every { Uri.encode(any()) } answers { firstArg<String>() }
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(Uri::class)
    }

    @Test
    fun `全ルートが一意である`() {
        val routeStrings = allRoutes.map { it.route }
        assertEquals(routeStrings.size, routeStrings.toSet().size)
    }

    @Test
    fun `全画面分のルートが13個定義されている`() {
        assertEquals(13, allRoutes.size)
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

    @Test
    fun `PrintGuideのルートが正しい`() {
        assertEquals("printGuide", NoshiRoute.PrintGuide.route)
    }

    @Test
    fun `PrintInfoのルートが正しい`() {
        assertEquals("printInfo", NoshiRoute.PrintInfo.route)
    }
}
