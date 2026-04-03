package jp.marginalgains.fastnoshi.rendering

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import java.io.File
import jp.marginalgains.fastnoshi.domain.model.NoshiTemplate

/**
 * のし紙レンダラー
 * テンプレートPDF/PNGを背景として読み込み、縦書きテキストを合成する。
 */
class NoshiRenderer(
    private val context: Context,
    private val verticalTextRenderer: VerticalTextRenderer = VerticalTextRenderer()
) {

    /**
     * テンプレート背景 + テキストを合成してBitmapを生成する
     * @param template のし紙テンプレート
     * @param omoteGaki 表書きテキスト
     * @param names 名前リスト
     * @param typeface 使用フォント
     * @param fontSize ベースフォントサイズ
     * @param paperSize 用紙サイズ
     * @return 合成されたBitmap
     */
    fun renderToBitmap(
        template: NoshiTemplate,
        omoteGaki: String,
        names: List<String>,
        typeface: Typeface?,
        omoteGakiFontSize: Float,
        nameFontSize: Float,
        paperSize: PaperSize
    ): Bitmap {
        val widthPx = paperSize.widthPt.toInt()
        val heightPx = paperSize.heightPt.toInt()

        val bitmap = Bitmap.createBitmap(widthPx, heightPx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // 白背景
        canvas.drawColor(Color.WHITE)

        // テンプレート背景描画
        drawTemplateBackground(canvas, template, widthPx, heightPx)

        // フォントサイズをスケーリング
        val scale = NoshiPdfGenerator.calculateScale(paperSize)
        val scaledOmoteGakiFontSize = omoteGakiFontSize * scale
        val scaledNameFontSize = nameFontSize * scale

        // テキスト合成
        verticalTextRenderer.renderOmoteGaki(
            canvas,
            omoteGaki,
            scaledOmoteGakiFontSize,
            widthPx.toFloat(),
            heightPx.toFloat(),
            typeface
        )
        verticalTextRenderer.renderNames(
            canvas,
            names,
            scaledNameFontSize,
            widthPx.toFloat(),
            heightPx.toFloat(),
            typeface
        )

        return bitmap
    }

    /**
     * テンプレートPNG画像を背景として描画する
     */
    private fun drawTemplateBackground(
        canvas: Canvas,
        template: NoshiTemplate,
        widthPx: Int,
        heightPx: Int
    ) {
        val bgBitmap = loadTemplatePng(template.pngFileName) ?: return
        val scaled = Bitmap.createScaledBitmap(bgBitmap, widthPx, heightPx, true)
        canvas.drawBitmap(scaled, 0f, 0f, null)
        if (scaled !== bgBitmap) scaled.recycle()
        bgBitmap.recycle()
    }

    /**
     * assetsからテンプレートPNG画像を読み込む
     */
    private fun loadTemplatePng(pngFileName: String): Bitmap? = try {
        context.assets.open("templates/$pngFileName").use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        }
    } catch (_: Exception) {
        null
    }
}
