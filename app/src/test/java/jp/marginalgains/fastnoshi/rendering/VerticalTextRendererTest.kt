package jp.marginalgains.fastnoshi.rendering

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class VerticalTextRendererTest {

    private val renderer = VerticalTextRenderer()

    // Canvas dimensions for testing (A4 landscape in pt)
    private val canvasWidth = 841.89f
    private val canvasHeight = 595.28f

    @Test
    fun `表書きのY開始位置はキャンバス高さの15パーセント`() {
        val fontSize = 24f
        val positions = renderer.calculateOmoteGakiPositions(
            text = "御祝",
            fontSize = fontSize,
            canvasWidth = canvasWidth,
            canvasHeight = canvasHeight
        )
        val expectedY = canvasHeight * 0.15f
        assertEquals(expectedY, positions.first().y, 0.01f)
    }

    @Test
    fun `表書きの文字間隔はfontSizeの1_1倍`() {
        val fontSize = 24f
        val positions = renderer.calculateOmoteGakiPositions(
            text = "御出産祝",
            fontSize = fontSize,
            canvasWidth = canvasWidth,
            canvasHeight = canvasHeight
        )
        val expectedSpacing = fontSize * 1.1f
        assertEquals(expectedSpacing, positions[1].y - positions[0].y, 0.01f)
    }

    @Test
    fun `表書きはキャンバスの水平中央に配置される`() {
        val fontSize = 24f
        val positions = renderer.calculateOmoteGakiPositions(
            text = "御祝",
            fontSize = fontSize,
            canvasWidth = canvasWidth,
            canvasHeight = canvasHeight
        )
        val x = positions.first().x
        positions.forEach { pos ->
            assertEquals(x, pos.x, 0.01f)
        }
        assertTrue(x > canvasWidth * 0.3f && x < canvasWidth * 0.7f)
    }

    @Test
    fun `名前セクションのY開始位置はキャンバス高さの62パーセント`() {
        val fontSize = 18f
        val positions = renderer.calculateNamePositions(
            names = listOf("太郎"),
            fontSize = fontSize,
            canvasWidth = canvasWidth,
            canvasHeight = canvasHeight
        )
        val expectedY = canvasHeight * 0.62f
        assertEquals(expectedY, positions.first().first().y, 0.01f)
    }

    @Test
    fun `複数名は右から左へ列配置される`() {
        val fontSize = 18f
        val positions = renderer.calculateNamePositions(
            names = listOf("太郎", "花子"),
            fontSize = fontSize,
            canvasWidth = canvasWidth,
            canvasHeight = canvasHeight
        )
        val taroX = positions[0].first().x
        val hanakoX = positions[1].first().x
        assertTrue(taroX > hanakoX, "右から左: 太郎のX($taroX) > 花子のX($hanakoX)")
    }

    @Test
    fun `複数名の列間スペースはfontSizeの0_8倍`() {
        val fontSize = 18f
        val positions = renderer.calculateNamePositions(
            names = listOf("太郎", "花子"),
            fontSize = fontSize,
            canvasWidth = canvasWidth,
            canvasHeight = canvasHeight
        )
        val taroX = positions[0].first().x
        val hanakoX = positions[1].first().x
        // columnStep = columnWidth(fontSize*1.2) + columnSpacing(fontSize*0.8)
        val expectedColumnStep = fontSize * 1.2f + fontSize * 0.8f
        assertEquals(expectedColumnStep, taroX - hanakoX, 0.1f)
    }

    @Test
    fun `1名の場合は中央に配置される`() {
        val fontSize = 18f
        val positions = renderer.calculateNamePositions(
            names = listOf("太郎"),
            fontSize = fontSize,
            canvasWidth = canvasWidth,
            canvasHeight = canvasHeight
        )
        val x = positions[0].first().x
        assertTrue(x > canvasWidth * 0.3f && x < canvasWidth * 0.7f)
    }

    @Test
    fun `5名まで対応できる`() {
        val fontSize = 14f
        val positions = renderer.calculateNamePositions(
            names = listOf("太郎", "花子", "次郎", "三郎", "四郎"),
            fontSize = fontSize,
            canvasWidth = canvasWidth,
            canvasHeight = canvasHeight
        )
        assertEquals(5, positions.size)
        for (i in 0 until positions.size - 1) {
            assertTrue(
                positions[i].first().x > positions[i + 1].first().x,
                "名前${i}のX > 名前${i + 1}のX"
            )
        }
    }

    @Test
    fun `名前の文字ごとの縦方向間隔はfontSizeの1_1倍`() {
        val fontSize = 18f
        val positions = renderer.calculateNamePositions(
            names = listOf("田中太郎"),
            fontSize = fontSize,
            canvasWidth = canvasWidth,
            canvasHeight = canvasHeight
        )
        val charPositions = positions[0]
        assertEquals(4, charPositions.size)
        val expectedSpacing = fontSize * 1.1f
        assertEquals(expectedSpacing, charPositions[1].y - charPositions[0].y, 0.01f)
    }
}
