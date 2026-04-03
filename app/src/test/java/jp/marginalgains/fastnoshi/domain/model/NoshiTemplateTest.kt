package jp.marginalgains.fastnoshi.domain.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class NoshiTemplateTest {

    @Test
    fun `全テンプレートは4種類`() {
        assertEquals(4, NoshiTemplate.all.size)
    }

    @Test
    fun `紅白蝶結びテンプレートの定義が正しい`() {
        val t = NoshiTemplate.findById("05_cho_red_on")
        assertNotNull(t)
        assertEquals("紅白蝶結び（のし付き）", t!!.name)
        assertTrue(t.isColor)
        assertTrue(t.omoteGakiCandidates.contains("御祝"))
        assertTrue(t.omoteGakiCandidates.contains("御出産祝"))
        assertTrue(t.omoteGakiCandidates.contains("御入学祝"))
        assertTrue(t.omoteGakiCandidates.contains("御新築祝"))
        assertEquals("05_cho_red_on.pdf", t.pdfFileName)
        assertEquals("05_cho_red_on.png", t.pngFileName)
    }

    @Test
    fun `紅白結び切りテンプレートの定義が正しい`() {
        val t = NoshiTemplate.findById("05_musu_red_off")
        assertNotNull(t)
        assertEquals("紅白結び切り（のしなし）", t!!.name)
        assertTrue(t.isColor)
        assertTrue(t.omoteGakiCandidates.contains("快気祝"))
        assertTrue(t.omoteGakiCandidates.contains("御見舞御礼"))
    }

    @Test
    fun `10本結び切りテンプレートの定義が正しい`() {
        val t = NoshiTemplate.findById("10_musu_red_on")
        assertNotNull(t)
        assertEquals("紅白結び切り10本（のし付き）", t!!.name)
        assertTrue(t.isColor)
        assertTrue(t.omoteGakiCandidates.contains("寿"))
        assertTrue(t.omoteGakiCandidates.contains("御結婚祝"))
    }

    @Test
    fun `黒白結び切りテンプレートの定義が正しい`() {
        val t = NoshiTemplate.findById("05_musu_black_off")
        assertNotNull(t)
        assertEquals("黒白結び切り", t!!.name)
        assertTrue(!t.isColor)
        assertTrue(t.omoteGakiCandidates.contains("御霊前"))
        assertTrue(t.omoteGakiCandidates.contains("御仏前"))
    }

    @Test
    fun `存在しないIDはnullを返す`() {
        assertNull(NoshiTemplate.findById("nonexistent"))
    }

    @Test
    fun `全テンプレートのIDはユニーク`() {
        val ids = NoshiTemplate.all.map { it.id }
        assertEquals(ids.size, ids.distinct().size)
    }

    @Test
    fun `全テンプレートにPDFとPNGファイル名が設定されている`() {
        NoshiTemplate.all.forEach { t ->
            assertTrue(t.pdfFileName.endsWith(".pdf"), "${t.id}: pdfFileName should end with .pdf")
            assertTrue(t.pngFileName.endsWith(".png"), "${t.id}: pngFileName should end with .png")
        }
    }
}
