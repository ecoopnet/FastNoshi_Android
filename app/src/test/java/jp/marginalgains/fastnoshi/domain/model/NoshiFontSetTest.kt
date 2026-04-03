package jp.marginalgains.fastnoshi.domain.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class NoshiFontSetTest {

    @Test
    fun `全フォントは3種類`() {
        assertEquals(3, NoshiFontSet.all.size)
    }

    @Test
    fun `明朝体の定義が正しい`() {
        val f = NoshiFontSet.findById("mincho")
        assertNotNull(f)
        assertEquals("明朝体", f!!.displayName)
        assertEquals("Noto Serif JP", f.fontFamily)
    }

    @Test
    fun `明朝体太字の定義が正しい`() {
        val f = NoshiFontSet.findById("mincho_bold")
        assertNotNull(f)
        assertEquals("明朝体（太字）", f!!.displayName)
        assertEquals("Noto Serif JP", f.fontFamily)
    }

    @Test
    fun `ゴシック体の定義が正しい`() {
        val f = NoshiFontSet.findById("gothic")
        assertNotNull(f)
        assertEquals("ゴシック体", f!!.displayName)
        assertEquals("Noto Sans JP", f.fontFamily)
    }

    @Test
    fun `存在しないIDはnullを返す`() {
        assertNull(NoshiFontSet.findById("nonexistent"))
    }

    @Test
    fun `全フォントのIDはユニーク`() {
        val ids = NoshiFontSet.all.map { it.id }
        assertEquals(ids.size, ids.distinct().size)
    }

    @Test
    fun `デフォルトフォントは明朝体`() {
        assertEquals("mincho", NoshiFontSet.default.id)
    }
}
