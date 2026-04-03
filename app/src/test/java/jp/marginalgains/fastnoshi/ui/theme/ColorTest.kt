package jp.marginalgains.fastnoshi.ui.theme

import androidx.compose.ui.graphics.Color
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ColorTest {

    @Test
    fun `Lightテーマのプライマリが茶色`() {
        assertEquals(Color(0xFF8B4513), NoshiBrown)
    }

    @Test
    fun `Lightテーマのセカンダリが紅色`() {
        assertEquals(Color(0xFFDC143C), NoshiRed)
    }

    @Test
    fun `Lightテーマのテーシャリが金色`() {
        assertEquals(Color(0xFFD4AF37), NoshiGold)
    }

    @Test
    fun `Darkテーマの金色が明るい`() {
        assertEquals(Color(0xFFFFD700), NoshiGoldDark)
    }
}
