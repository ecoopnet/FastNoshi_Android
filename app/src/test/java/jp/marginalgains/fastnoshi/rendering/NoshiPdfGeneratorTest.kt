package jp.marginalgains.fastnoshi.rendering

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class NoshiPdfGeneratorTest {

    @Test
    fun `PaperSizeのpt変換は72DPI基準`() {
        // 1mm = 72/25.4 pt
        val mmToPt = 72.0f / 25.4f
        PaperSize.entries.forEach { size ->
            assertEquals(
                size.widthMm * mmToPt,
                size.widthPt,
                0.01f,
                "${size.name}: widthPt"
            )
            assertEquals(
                size.heightMm * mmToPt,
                size.heightPt,
                0.01f,
                "${size.name}: heightPt"
            )
        }
    }

    @Test
    fun `A4基準のスケール計算が正しい`() {
        val a4WidthPt = PaperSize.A4.widthPt
        val a4HeightPt = PaperSize.A4.heightPt

        // A4 → A4: scale = 1.0
        val scaleA4 = NoshiPdfGenerator.calculateScale(PaperSize.A4)
        assertEquals(1.0f, scaleA4, 0.001f)

        // A3: scale > 1.0
        val scaleA3 = NoshiPdfGenerator.calculateScale(PaperSize.A3)
        assertTrue(scaleA3 > 1.0f, "A3 scale should be > 1.0")
        val expectedA3Scale = minOf(
            PaperSize.A3.widthPt / a4WidthPt,
            PaperSize.A3.heightPt / a4HeightPt
        )
        assertEquals(expectedA3Scale, scaleA3, 0.001f)

        // B4: scale > 1.0
        val scaleB4 = NoshiPdfGenerator.calculateScale(PaperSize.B4)
        assertTrue(scaleB4 > 1.0f, "B4 scale should be > 1.0")
    }
}
