package jp.marginalgains.fastnoshi.rendering

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.text.TextPaint

/**
 * 縦書きテキストレンダラー
 * Canvas + TextPaint で1文字ずつ縦書き描画する。
 * 複数名は右から左へ列配置（日本語正統縦書き）。
 */
class VerticalTextRenderer {

    /** 描画位置（Android非依存でテスト可能） */
    data class Position(val x: Float, val y: Float)

    companion object {
        /** 文字間隔: fontSize × 1.1 */
        const val CHAR_SPACING_RATIO = 1.1f
        /** 列間スペース: fontSize × 0.8 */
        const val COLUMN_SPACING_RATIO = 0.8f
        /** 1列の幅: fontSize × 1.2 */
        const val COLUMN_WIDTH_RATIO = 1.2f
        /** 表書き上部マージン: キャンバス高さ × 0.15 */
        const val OMOTE_GAKI_TOP_MARGIN_RATIO = 0.15f
        /** 名前セクション上部マージン: キャンバス高さ × 0.62 */
        const val NAME_SECTION_TOP_MARGIN_RATIO = 0.62f
    }

    /**
     * 表書きの各文字の描画位置を計算する
     * @return 各文字の描画位置（左上基準）
     */
    fun calculateOmoteGakiPositions(
        text: String,
        fontSize: Float,
        canvasWidth: Float,
        canvasHeight: Float
    ): List<Position> {
        val columnWidth = fontSize * COLUMN_WIDTH_RATIO
        val centerX = (canvasWidth - columnWidth) / 2f + columnWidth / 2f - fontSize / 2f
        val topY = canvasHeight * OMOTE_GAKI_TOP_MARGIN_RATIO
        val charSpacing = fontSize * CHAR_SPACING_RATIO

        return text.indices.map { i ->
            Position(centerX, topY + i * charSpacing)
        }
    }

    /**
     * 名前セクションの各文字の描画位置を計算する
     * @param names 名前リスト（右から左へ配置）
     * @return 名前ごとの文字位置リスト。外側リストが名前、内側リストが各文字。
     */
    fun calculateNamePositions(
        names: List<String>,
        fontSize: Float,
        canvasWidth: Float,
        canvasHeight: Float
    ): List<List<Position>> {
        if (names.isEmpty()) return emptyList()

        val columnWidth = fontSize * COLUMN_WIDTH_RATIO
        val columnSpacing = fontSize * COLUMN_SPACING_RATIO
        val columnStep = columnWidth + columnSpacing
        val totalWidth = names.size * columnWidth +
            (names.size - 1).coerceAtLeast(0) * columnSpacing
        val startX = (canvasWidth + totalWidth) / 2f - columnWidth / 2f - fontSize / 2f
        val topY = canvasHeight * NAME_SECTION_TOP_MARGIN_RATIO
        val charSpacing = fontSize * CHAR_SPACING_RATIO

        return names.indices.map { nameIndex ->
            val name = names[nameIndex]
            val columnX = startX - nameIndex * columnStep
            name.indices.map { charIndex ->
                Position(columnX, topY + charIndex * charSpacing)
            }
        }
    }

    /**
     * 表書きをCanvasに描画する
     */
    fun renderOmoteGaki(
        canvas: Canvas,
        text: String,
        fontSize: Float,
        canvasWidth: Float,
        canvasHeight: Float,
        typeface: Typeface? = null
    ) {
        val paint = createTextPaint(fontSize, typeface)
        val positions = calculateOmoteGakiPositions(text, fontSize, canvasWidth, canvasHeight)
        text.forEachIndexed { i, char ->
            canvas.drawText(
                char.toString(),
                positions[i].x,
                positions[i].y + fontSize,
                paint
            )
        }
    }

    /**
     * 名前セクションをCanvasに描画する
     */
    fun renderNames(
        canvas: Canvas,
        names: List<String>,
        fontSize: Float,
        canvasWidth: Float,
        canvasHeight: Float,
        typeface: Typeface? = null
    ) {
        val paint = createTextPaint(fontSize, typeface)
        val allPositions = calculateNamePositions(names, fontSize, canvasWidth, canvasHeight)
        names.forEachIndexed { nameIndex, name ->
            val positions = allPositions[nameIndex]
            name.forEachIndexed { charIndex, char ->
                canvas.drawText(
                    char.toString(),
                    positions[charIndex].x,
                    positions[charIndex].y + fontSize,
                    paint
                )
            }
        }
    }

    private fun createTextPaint(fontSize: Float, typeface: Typeface?): TextPaint =
        TextPaint().apply {
            textSize = fontSize
            color = Color.BLACK
            isAntiAlias = true
            typeface?.let { this.typeface = it }
        }
}
