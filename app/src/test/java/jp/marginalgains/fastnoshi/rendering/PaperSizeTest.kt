package jp.marginalgains.fastnoshi.rendering

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class PaperSizeTest {

    @ParameterizedTest
    @CsvSource(
        "A4, 297.0, 210.0",
        "A3, 420.0, 297.0",
        "B4, 364.0, 257.0"
    )
    fun `用紙サイズのmm値が正しい`(name: String, expectedWidth: Double, expectedHeight: Double) {
        val size = PaperSize.valueOf(name)
        assertEquals(expectedWidth, size.widthMm.toDouble(), 0.01)
        assertEquals(expectedHeight, size.heightMm.toDouble(), 0.01)
    }

    @ParameterizedTest
    @CsvSource(
        "A4, 841.89, 595.28",
        "A3, 1190.55, 841.89",
        "B4, 1031.81, 728.50"
    )
    fun `用紙サイズのpt値が正しい`(name: String, expectedWidth: Double, expectedHeight: Double) {
        val size = PaperSize.valueOf(name)
        assertEquals(expectedWidth, size.widthPt.toDouble(), 0.1)
        assertEquals(expectedHeight, size.heightPt.toDouble(), 0.1)
    }

    @Test
    fun `全3種類の用紙サイズが存在する`() {
        assertEquals(3, PaperSize.entries.size)
    }

    @Test
    fun `fromString で文字列から変換できる`() {
        assertNotNull(PaperSize.fromString("A4"))
        assertNotNull(PaperSize.fromString("A3"))
        assertNotNull(PaperSize.fromString("B4"))
        assertEquals(null, PaperSize.fromString("B5"))
    }

    @ParameterizedTest
    @CsvSource(
        "A4, 0",
        "A3, 1",
        "B4, 3"
    )
    fun `NPS APIコード値が正しい`(name: String, expectedCode: String) {
        val size = PaperSize.valueOf(name)
        assertEquals(expectedCode, size.npsCode)
    }

    @Test
    fun `landscapeでwidthがheightより大きい`() {
        PaperSize.entries.forEach { size ->
            assert(size.widthMm > size.heightMm) {
                "${size.name}: landscape の場合 width > height"
            }
        }
    }
}
